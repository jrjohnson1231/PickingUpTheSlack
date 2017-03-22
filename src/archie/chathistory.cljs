(ns chathistory.core
  (:require [clojure.string :as str]))
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
