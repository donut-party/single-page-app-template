(ns {{top/ns}}.{{main/ns}}.frontend.frontend-routes
  (:require
   [donut.frontend.events :as dfe]
   [donut.frontend.routes :as dfr]
   [donut.frontend.sync.flow :as dsf]
   [{{top/ns}}.{{main/ns}}.frontend.components.home :as h]))

(dfr/defroutes routes
  [["/"
    {:name       :home
     :components {:main [h/component]}}]])
