(ns chathistory.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [clojure.string :as str])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]])
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

(go (let [response (<! (http/get "/channels"
                                 {:with-credentials? false
                                  :query-params {"since" 135}}))]
      (prn (:status response))
      (prn (map :login (:body response)))))
