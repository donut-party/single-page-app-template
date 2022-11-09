(ns donut.minimal.frontend.core
  (:require
   [donut.frontend.config :as dconf]
   [donut.frontend.core.flow :as dcf]
   [donut.frontend.core.utils :as dcu]
   [donut.frontend.nav.flow :as dnf]
   [donut.system :as ds]
   [donut.minimal.cross.endpoint-routes :as endpoint-routes]
   [donut.minimal.frontend.app :as app]
   [donut.minimal.frontend.frontend-routes :as frontend-routes]
   [donut.minimal.frontend.subs] ;; load subs
   [meta-merge.core :as meta-merge]
   [re-frame.core :as rf]
   [reagent.dom :as rdom]))

(defn system-config
  "This is a function instead of a static value so that it will pick up
  reloaded changes"
  []
  (meta-merge/meta-merge
   dconf/default-config
   {::ds/defs
    {:donut.frontend
     {:sync-router     #::ds{:config {:routes endpoint-routes/routes}}
      :frontend-router #::ds{:config {:routes frontend-routes/routes}}}}}))

(defn ^:dev/after-load start []
  (rf/dispatch-sync [::dcf/start-system (system-config)])
  (rf/dispatch-sync [::dnf/dispatch-current])
  (rdom/render [app/app] (dcu/el-by-id "app")))

(defn init
  []
  (start))

(defn ^:dev/before-load stop [_]
  (rf/dispatch-sync [::dcf/stop-system]))
