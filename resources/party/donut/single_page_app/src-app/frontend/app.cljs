(ns {{top/ns}}.{{main/ns}}.frontend.app
  (:require
   [donut.frontend.nav.components :as dnc]
   [donut.frontend.nav.flow :as dnf]
   [{{top/ns}}.{{main/ns}}.frontend.handlers] ;; load handlers
   [re-frame.core :as rf]))

(defn main-nav
  []
  [:ul {:class "menu w-full grow mt-1"}
   [:li
    [dnc/simple-route-link {:class      "is-drawer-close:tooltip is-drawer-close:tooltip-right"
                            :data-tip   "Homepage"
                            :route-name :home}
     [:svg {:xmlns "http://www.w3.org/2000/svg", :viewBox "0 0 24 24", :stroke-linejoin "round", :stroke-linecap "round", :stroke-width "2", :fill "none", :stroke "currentColor", :class "my-1.5 inline-block size-4"}
      [:path {:d "M15 21v-8a1 1 0 0 0-1-1h-4a1 1 0 0 0-1 1v8"}]
      [:path {:d "M3 10a2 2 0 0 1 .709-1.528l7-5.999a2 2 0 0 1 2.582 0l7 5.999A2 2 0 0 1 21 10v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"}]]
     [:span {:class "is-drawer-close:hidden"} "Homepage"]]]])

(defn app
  []
  [:div {:class "drawer lg:drawer-open"}
   [:input {:id "my-drawer-4", :type "checkbox", :class "drawer-toggle"}]

   ;; right-hand section
   [:div {:class "drawer-content"}
    [:nav {:class "navbar w-full bg-green-300"}
     [:label {:for "my-drawer-4", :aria-label "open sidebar", :class "btn btn-square btn-ghost"}
      [:svg {:xmlns "http://www.w3.org/2000/svg", :viewBox "0 0 24 24", :stroke-linejoin "round", :stroke-linecap "round", :stroke-width "2", :fill "none", :stroke "currentColor", :class "my-1.5 inline-block size-4"}
       [:path {:d "M4 4m0 2a2 2 0 0 1 2 -2h12a2 2 0 0 1 2 2v12a2 2 0 0 1 -2 2h-12a2 2 0 0 1 -2 -2z"}]
       [:path {:d "M9 4v16"}]
       [:path {:d "M14 10l2 2l-2 2"}]]]
     [:div {:class "px-4"} "Navbar Title"]]
    [:div {:class "px-4 py-8"}
     ;; main content
     [:div
      @(rf/subscribe [::dnf/routed-component :main])]]]

   ;; side drawer
   [:div {:class "drawer-side is-drawer-close:overflow-visible"}
    [:label {:for "my-drawer-4", :aria-label "close sidebar", :class "drawer-overlay"}]
    [:div {:class "flex min-h-full flex-col items-start bg-green-200 is-drawer-close:w-14 is-drawer-open:w-64"}
     [main-nav]]]])
