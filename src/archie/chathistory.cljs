(ns chathistory.core)
((defn openTab
  [evt, tabName]
  (let [tabcontent (js/document.getElementsByClassName "tabcontent")])
  (dotimes [i (.-length tabcontent)] (aset tabcontent i "style" "display" "none"))
  (let [tablinks (js/document.getElementsByClassName "tablinks")])
  (dotimes [i (.-length tablinks)] (aset tablinks i "className" (replace (aget tablinks i "className") " active" "")))
  (let [cityName (js/document.getElementsById "cityName")])
  (aset cityName "style" "display" "block")
  (aset evt "currentTarget" "className" (str (aget evt "currentTarget" "className") " active"))
  ; (let [defaultOpen (js/document.getElementsById "defaultOpen")])
  ;
  ; (js/document.dispatchEvent )
))
