(ns {{top/ns}}.{{main/ns}}.backend.system
  (:require
   [aero.core :as aero]
   [clojure.java.io :as io]
   [donut.endpoint.middleware :as dem]
   [donut.endpoint.route-group :as derg]
   [donut.endpoint.router :as der]
   [donut.endpoint.test.harness :as deth]
   [{{top/ns}}.{{main/ns}}.cross.endpoint-routes :as endpoint-routes]
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

    :http
    {:server
     #::ds{:start  (fn [{:keys [::ds/config]}]
                     (rj/run-jetty (:handler config) (:options config)))
           :stop   (fn [{:keys [::ds/instance]}]
                     (.stop instance))
           :config {:handler (ds/local-ref [:handler])
                    :options {:port  (ds/ref [:env :http-port])
                              :join? false}}}

     :middleware
     dem/AppMiddlewareComponent

     :handler
     #::ds{:start  (fn [{:keys [::ds/config]}]
                     (let [{:keys [route-ring-handler middleware]} config]
                       (middleware route-ring-handler)))
           :config {:route-ring-handler (ds/ref [:routing :ring-handler])
                    :middleware         (ds/local-ref [:middleware])}}}

    :routing
    {:ring-handler der/RingHandlerComponent
     :router       der/RouterComponent
     :router-opts  der/router-opts
     :routes       [(ds/ref [:main-routes :route-group])]}

    :main-routes
    (derg/route-group
     {:group-path "/api/v1"
      :group-opts {:datasource (ds/ref [:db :datasource])}
      :routes     endpoint-routes/routes})

    :db
    {:datasource
     #::ds{:start  (fn [{:keys [::ds/config]}] (jdbc/get-datasource (:dbspec config)))
           :config {:dbspec (ds/ref [:env :dbspec])}}

     :migratus
     (ds/cache-component
      #::ds{:start  (fn [{:keys [::ds/config] :as s}]
                      (migratus/migrate config))
            :config {:run?          true
                     :db            (ds/local-ref [:datasource])
                     :store         :database
                     :migration-dir "migrations"}}
      (ds/ref [:env :profile-name]))}}

   ::ds/plugins [deth/test-harness-plugin]})

(defmethod ds/named-system :dev
  [_]
  (ds/system :base {[:env] (env-config :dev)}))

(defmethod ds/named-system :test
  [_]
  (ds/system :dev
    {[:env]
     (env-config :test)

     [:http :server]
     ::disabled

     [:http :middleware ::ds/config :security :anti-forgery]
     false}))
