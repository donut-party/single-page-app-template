(ns donut.minimal.fixtures
  (:require
   [donut.datapotato.next-jdbc :as dnj]
   [donut.minimal.backend.system] ;; for multimethod
   [donut.system :as ds]
   [malli.generator :as mg]
   [next.jdbc :as jdbc]))

(defn db-connection
  []
  (jdbc/get-connection (get-in ds/*system* [::ds/instances :db :datasource])))

(def potato-schema
  ;; TODO donut-generated remove this
  {:example-entity {:prefix   :ee
                    :generate {:schema [:map
                                        [:id pos-int?]
                                        [:description string?]]}
                    :fixtures {:table-name "example_entity"}}})

(def datapotato-db
  {:schema   potato-schema
   :generate {:generator mg/generate
              #_(comment
                  ;; use clojure spec generator
                  (comp clojure.spec.gen.alpha/generate clojure.spec.alpha/gen))}
   :fixtures (merge dnj/config
                    {:get-connection (fn [_] (db-connection))
                     :setup          (fn [{{:keys [connection]} :fixtures}]
                                       ;; clear tables
                                       (jdbc/execute! connection ["DELETE FROM example_entity"])
                                       )})})
