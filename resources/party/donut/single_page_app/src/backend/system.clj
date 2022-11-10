(ns {{top/ns}}.backend.system
  (:require
   [aero.core :as aero]
   [clojure.java.io :as io]
   [donut.middleware :as dm]
   [{{top/ns}}.backend.handler :as dh]
   [{{top/ns}}.cross.endpoint-routes :as endpoint-routes]
   [donut.system :as ds]
   [migratus.core :as migratus]
   [next.jdbc :as jdbc]
   [ring.adapter.jetty :as rj]))

(defn env-config [& [profile-name]]
  (-> "config/env.edn"
      io/resource
      (aero/read-config (when profile-name {:profile profile-name}))
      (assoc :profile-name profile-name)))

(defmethod ds/named-system :base
  [_]
  {::ds/defs
   {:env
    (env-config)

    :middleware
    (assoc dm/MiddlewareComponentGroup :routes endpoint-routes/routes)

    :http
    {:server
     #::ds{:start  (fn [{:keys [::ds/config]}]
                     (rj/run-jetty (:handler config) (:options config)))
           :stop   (fn [{:keys [::ds/instance]}]
                     (.stop instance))
           :config {:handler (ds/local-ref [:handler])
                    :options {:port  (ds/ref [:env :http-port])
                              :join? false}}}

     :handler
     #::ds{:start  (fn [{:keys [::ds/config]}] (dh/handler config))
           :config {:db         (ds/ref [:db :connection])
                    :router     (ds/ref [:middleware :router])
                    :middleware (ds/ref [:middleware :middleware])}}}

    :db
    {:connection
     #::ds{:start  (fn [{:keys [::ds/config]}] (jdbc/get-datasource (:dbspec config)))
           :config {:dbspec (ds/ref [:env :dbspec])}}

     :migratus
     #::ds{:start  (fn [{:keys [::ds/config]}]
                     (when (:run? config)
                       (migratus/migrate config)))
           :config {:run?          true
                    :db            (ds/local-ref [:connection])
                    :store         :database
                    :migration-dir "migrations"}}}}})

(defmethod ds/named-system :dev
  [_]
  (ds/system :base {[:env] (env-config :dev)}))

(defonce run-migrations? (atom true))

(defmethod ds/named-system :test
  [_]
  (ds/system :dev
    {[:env]
     (env-config :test)

     [:db :run-migrations? :start]
     (fn [_ _ _]
       (when @run-migrations?
         (reset! run-migrations? false)
         true))

     [:db :migratus ::ds/config :run?]
     (ds/local-ref [:run-migrations?])

     [:http :server]
     ::disabled

     [:http :middleware ::ds/config :security :anti-forgery]
     false}))
