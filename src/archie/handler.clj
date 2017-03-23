(ns archie.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [ring.middleware.defaults :refer :all]
            [ring.middleware.json :as json]
            [clj-http.client :as client]
            [monger.core :as mg]
            [monger.collection :as mc]
            [compojure.route :as route]
            monger.json)
  (:import [org.bson.types ObjectId]
           [com.mongodb MongoOptions ServerAddress]))

(use '[clojure.string :only (replace-first)])

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

(defn get_tags [message]
  (map #(replace-first % #"\s+\$" "") (re-seq #"\s+\$\w+" message))
  )

(defn handle_event [body]
  (let [uri     (System/getenv "MONGODB_URI")
        {:keys [conn db]} (mg/connect-via-uri uri)
        channel (get_channel_info (get-in body [:event :channel]))
        user    (get_user_info (get-in body [:event :user]))
        message (get-in body [:event :text])
        event_id (get-in body [:event :event_id])
        timestamp (get-in body [:event :event_ts])
        tags (get_tags message)
        message { :user user :channel channel :message message :timestamp timestamp :event_id event_id :tags tags }]
    (println "inserting document" message)
    (mc/update db "messages" {:user user :timestamp timestamp} message {:upsert true})
    (mc/update db "channels" {:name channel} {:name channel {$addToSet {:tags {$each {tags}}}}} {:upsert true})
    (mc/update db "users" {:name user} {:name user} {:upsert true})
    (println "done inserting")
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
  (GET "/channel/:channelName" [channelName]
      (let [uri     (System/getenv "MONGODB_URI")
              {:keys [conn db]} (mg/connect-via-uri uri)]
          (mc/find-one-as-map db "channels" {:name channelName})
          )
       )
  (GET "/user/:userName" [userName]
      (let [uri     (System/getenv "MONGODB_URI")
              {:keys [conn db]} (mg/connect-via-uri uri)]
          (mc/find-one-as-map db "users" {:name userName})
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
  (GET "/messages/:eventID" [eventID]
       (let [uri     (System/getenv "MONGODB_URI")
             {:keys [conn db]} (mg/connect-via-uri uri)]
         (mc/find-one-as-map db "messages" {:eventID eventID})
         )
       )
  (GET "/messages" []
       (let [uri     (System/getenv "MONGODB_URI")
             {:keys [conn db]} (mg/connect-via-uri uri)]
         (mc/find-maps db "messages")
         )
       )
 (GET "/messages/:channelID" [channelID]
      (let [uri     (System/getenv "MONGODB_URI")
            {:keys [conn db]} (mg/connect-via-uri uri)]
        (mc/find-maps db "messages" {:channel channelID})
        )
      )
  (GET "/messages/:channelID/:tag" [channelID, tag]
       (let [uri     (System/getenv "MONGODB_URI")
             {:keys [conn db]} (mg/connect-via-uri uri)]
         (mc/find-maps db "messages" {:channel channelID :tags tag})
         )
       )
  (route/resources "/")
  (route/not-found "Route Not Found"))

(def app
  (->
    (handler/site app-routes)
    (json/wrap-json-body {:keywords? true})
    json/wrap-json-response
    ))
