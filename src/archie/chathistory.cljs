(ns chathistory.core
  (:require [clojure.string :as str]))
(defn openTab
  [evt, tabName]
  (.log js/console evt)
  (let [tabcontent (js/document.getElementsByClassName "tabcontent")]
    (.log js/console tabcontent)
    (dotimes [i (.-length tabcontent)] (aset tabcontent i "style" "display" "none")))
  (let [tablinks (js/document.getElementsByClassName "tablinks")]
    (dotimes [i (.-length tablinks)] (aset tablinks i "className" (str/replace (aget tablinks i "className") "active" ""))))
  (let [cityName (js/document.getElementById "cityName")]
    (aset cityName "style" "display" "block")
    (aset evt "currentTarget" "className" (str (aget evt "currentTarget" "className") " active")))
  (let [defaultOpen (js/document.getElementsById "defaultOpen")])

  ;(js/document.dispatchEvent )
  )
