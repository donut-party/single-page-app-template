(ns {{top/ns}}.{{main/ns}}.fixtures
  (:require
   [donut.datapotato.next-jdbc :as dnj]
   [{{top/ns}}.{{main/ns}}.backend.system] ;; for multimethod
   [donut.system :as ds]
   [malli.generator :as mg]
   [next.jdbc :as jdbc]))

(defn db-connection
  []
  (jdbc/get-connection (get-in ds/*system* [::ds/instances :db :datasource])))

(def potato-schema
  ;; entries are added here by `donut generate entity-scaffold <name>`
  {})

(def datapotato-db
  {:schema   potato-schema
   :generate {:generator mg/generate}
   :fixtures (merge dnj/config
                    {:get-connection (fn [_] (db-connection))
                     :setup          (fn [{{:keys [connection]} :fixtures}]
                                       ;; clear every schema table so each test run starts fresh
                                       (doseq [{{:keys [table-name]} :fixtures} (vals potato-schema)]
                                         (jdbc/execute! connection [(str "DELETE FROM " table-name)])))})})
