(ns dev
  {:clj-kondo/config {:linters {:unused-namespace {:level :off}}}}
  (:require
   [clojure.tools.namespace.repl :as nsrepl]
   [dev.repl :as dev-repl]
   [donut.endpoint.test.harness :as deth]
   [donut.minimal.backend.system :as sys]
   [donut.routes :as dr]
   [donut.system :as ds]
   [donut.system.repl :as dsr]
   [donut.system.repl.state :as dsrs]
   [malli.core :as m]
   [migratus.core :as migratus]
   [muuntaja.core :as mu]
   [reitit.core :as r]
   [ring.mock.request :as ring-mock])
  (:refer-clojure :exclude [test]))

(nsrepl/set-refresh-dirs "dev/src" "src" "test")

(defn routes
  []
  (get-in dsrs/system [::ds/defs :http :routes]))

(def start dsr/start)
(def stop dsr/stop)
(def restart dsr/restart)

(defn db-config [] (get-in dsrs/system [::ds/instances :db :migratus]))
(defn router [] (get-in dsrs/system [::ds/instances :http :router]))

(defmethod ds/named-system :donut.system/repl
  [_]
  (ds/system :dev
    {[:db :migratus :conf :run?] (let [run? (not (:migrations-ran? @dev-repl/persistent-state))]
                                   (swap! dev-repl/persistent-state assoc :migrations-ran? true)
                                   run?)}))

(when-not dsrs/system
  (dsr/start))
