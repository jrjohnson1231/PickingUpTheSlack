(ns chathistory.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [clojure.string :as str]
            [cljs-http.client :as http]
            [goog.dom :as dom]
            [cognitect.transit :as t]
            [cljs.core.async :refer [<!]]))

(def r (t/reader :json))

(defn openTab
  [evt, tabName]
  (.log js/console (aget evt "currentTarget"))
  (let [tabcontent (js/document.getElementsByClassName "tabcontent")]
    (.log js/console tabcontent)
    (dotimes [i (.-length tabcontent)] (aset tabcontent i "style" "display" "none")))
  (let [tablinks (js/document.getElementsByClassName "tablinks")]
    (dotimes [i (.-length tablinks)] (aset tablinks i "className" (str/replace (aget tablinks i "className") "active" ""))))
  (let [tabEl (js/document.getElementById "tabName")]
    (aset evt "currentTarget" "className" (str (aget evt "currentTarget" "className") " active")))
  (let [defaultOpen (js/document.getElementById "defaultOpen")])

  ;(js/document.dispatchEvent )
  )

(defn create-tab [channelName]
  (.log js/console channelName)
  )

(go (let [response (<! (http/get "/channels"
                                 {:with-credentials? false
                                  :query-params {"since" 135}}))]
      (.log js/console "outside")
      (for [channel (t/read r (:body response))]
        (.log js/console "inside")
        ;(dom/appendChild (js/document.getElementsByClassName "tabs") (create-tab (get channel :name)))
        )))
