(ns {{top/ns}}.{{main/ns}}.frontend.core
  (:require
   [donut.frontend.config :as dconf]
   [donut.frontend.core.flow :as dcf]
   [donut.frontend.core.utils :as dcu]
   [donut.frontend.nav.flow :as dnf]
   [donut.system :as ds]
   [{{top/ns}}.{{main/ns}}.cross.endpoint-routes :as endpoint-routes]
   [{{top/ns}}.{{main/ns}}.frontend.app :as app]
   [{{top/ns}}.{{main/ns}}.frontend.frontend-routes :as frontend-routes]
   [{{top/ns}}.{{main/ns}}.frontend.subs] ;; load subs
   [meta-merge.core :as meta-merge]
   [re-frame.core :as rf]
   [reagent.dom :as rdom]))

(defmethod ds/named-system :frontend
  [_]
  (meta-merge/meta-merge
   dconf/default-config
   {::ds/defs
    {:donut.frontend
     {:sync-router     #::ds{:config {:routes endpoint-routes/routes}}
      :frontend-router #::ds{:config {:routes frontend-routes/routes}}}}}))

(defmethod ds/named-system :frontend-dev
  [_]
  (ds/system :frontend
    ;; add dev routes
    {[:donut.frontend :frontend-router ::ds/config :routes]
     (into frontend-routes/routes [])}))

(defn ^:dev/after-load start []
  (rf/dispatch-sync [::dcf/start-system (ds/system :frontend-dev)])
  (rf/dispatch-sync [::dnf/dispatch-current])
  (rdom/render [app/app] (dcu/el-by-id "app")))

(defn init
  []
  (start))

(defn ^:dev/before-load stop [_]
  (rf/dispatch-sync [::dcf/stop-system]))
