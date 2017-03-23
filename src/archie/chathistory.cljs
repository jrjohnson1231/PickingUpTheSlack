 (ns chathistory.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [clojure.string :as str]
            [cljs-http.client :as http]
            [goog.dom :as dom]
            [cognitect.transit :as t]
            [cljs.core.async :refer [<!]]))
;
; (def r (t/reader :json))
;
; (defn openTab
;   [evt, tabName]
;   (.log js/console (aget evt "currentTarget"))
;   (let [tablinks (js/document.getElementsByClassName "tablinks")]
;     (dotimes [i (.-length tablinks)] (aset tablinks i "className" (str/replace (aget tablinks i "className") "active" ""))))
;   (let [tabEl (js/document.getElementById "tabName")]
;     (aset evt "currentTarget" "className" (str (aget evt "currentTarget" "className") " active")))
;   (let [defaultOpen (js/document.getElementById "defaultOpen")])
;
;   (js/document.dispatchEvent )
;   )
