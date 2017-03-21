(ns archie.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [ring.middleware.defaults :refer :all]
            [ring.middleware.json :as json]
            [clj-http.client :as client]
            [monger.core :as mg]
            [monger.collection :as mc]
            [compojure.route :as route])
  (:import [org.bson.types ObjectId]
           [com.mongodb MongoOptions ServerAddress]))

(def auth_token
  (System/getenv "AUTH_TOKEN")
  )

(defn get_channel_info [channelID]
  (get-in (client/get "https://slack.com/api/channels.info" {:as :json, :query-params {:token auth_token, :channel channelID}}) [:body :channel :name])
  )

(defn get_user_info [userID]
  (get-in (client/get "https://slack.com/api/users.info" {:as :json, :query-params {:token auth_token, :user userID}}) [:body :user :name])
  )

(defn handle_challenge [body]
  (get body :challenge)
  )

(defn handle_event [body]
  (let [uri     (System/getenv "MONGODB_URI")
                {:keys [conn db]} (mg/connect-via-uri uri)
        channel (get_channel_info (get-in body [:event :channel]))
        user    (get_user_info (get-in body [:event :user]))
        message (get-in body [:event :text])
        event_id (get-in body [:event :event_id])
        timestamp (get-in body [:event :event_ts])
        doc { :_id (ObjectId.) :user user :channel channel :message message :timestamp timestamp :event_id event_id }]
    (println "inserting document" doc)
    (mc/update db "messages" {:event_id event_id} message {:upsert true})
    (mc/update db "channels" {:name channel} {:name channel} {:upsert true})
    (mc/update db "users" {:name user} {:name user} {:upsert true})
    )
  )

(defroutes app-routes
  (GET "/" [] "Hello World")
  (POST "/slack" req
        (let [body (get req :body)]
          (println body)
          (case (get body :type)
            "url_verification" (handle_challenge body)
            "event_callback" (handle_event body)
            )
          ))
  (GET "/channel/:channelName" )
      (let [uri     (System/getenv "MONGODB_URI")
              {:keys [conn db]} (mg/connect-via-uri uri)
              {{channelName :channelName} :params}]
          (mc/find-maps db "channels" {:name channelName})
          )
  (GET "/user/:userName"
      (let [uri     (System/getenv "MONGODB_URI")
              {:keys [conn db]} (mg/connect-via-uri uri)
              {{userName :userName} :params}]
          (mc/find-maps db "users" {:name userName})
          )
      )
  (GET "/channels" []
       (let [uri     (System/getenv "MONGODB_URI")
             {:keys [conn db]} (mg/connect-via-uri uri)]
         (mc/find-maps db "channels")
         )
       )
  (GET "/users" []
      (let [uri     (System/getenv "MONGODB_URI")
            {:keys [conn db]} (mg/connect-via-uri uri)]
        (mc/find-maps db "users")
        )
      )
  (GET "/messages/:eventID"
      (let [uri     (System/getenv "MONGODB_URI")
              {:keys [conn db]} (mg/connect-via-uri uri)
              {{eventID :eventID} :params}]
          (mc/find-maps db "messages" {:eventID eventID})
          )
      )
  (GET "/messages" []
       (let [uri     (System/getenv "MONGODB_URI")
             {:keys [conn db]} (mg/connect-via-uri uri)]
         (mc/find-maps db "messages")
         )
       )
  (route/not-found "Route Not Found"))

(def app
  (->
    (handler/site app-routes)
    (json/wrap-json-body {:keywords? true})
    json/wrap-json-response
    ))
