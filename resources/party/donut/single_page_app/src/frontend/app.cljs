(ns donut.minimal.frontend.app
  (:require
   [re-frame.core :as rf]
   [donut.frontend.nav.components :as dnc]
   [donut.frontend.nav.flow :as dnf]
   [donut.minimal.frontend.handlers] ;; load handlers
   [donut.minimal.frontend.ui :as ui]))

(defn app
  []
  [:div
   [:div
    [:main {:class "flex-1"}
     [:div {:class "max-w-2xl mx-auto"
            :role  "topnav"}
      [ui/link
       [dnc/simple-route-link {:route-name :home}
        "home"]]]
     [:div {:class "max-w-2xl mx-auto mt-4"}
      @(rf/subscribe [::dnf/routed-component :main])]]]])
