(ns {{top/ns}}.{{main/ns}}.frontend.components.home
  (:require
   [{{top/ns}}.{{main/ns}}.frontend.ui :as ui]))

(defn component
  []
  [:div {:class "prose"}
   [ui/h1 "A fresh new 🍩 app!"]
   [:p "Try this to get started:"]
   [:pre
    [:code "donut g endpoint entity"]]])
