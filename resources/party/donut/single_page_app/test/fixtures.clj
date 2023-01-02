(ns {{top/ns}}.{{main/ns}}.fixtures
  (:require
   [donut.datapotato.next-jdbc :as dfn]
   [donut.endpoint.test.harness :as deth]
   [{{top/ns}}.{{main/ns}}.backend.system] ;; for multimethod
   [donut.system :as ds]
   [malli.generator :as mg]
   [next.jdbc :as jdbc]))

(defn db-connection
  []
  (jdbc/get-connection (get-in deth/*system* [::ds/instances :db :datasource])))

(def potato-schema
  ;; TODO donut-generated remove this
  {:example-entity {:prefix   :ee
                    :generate {:schema [:map]}
                    :fixtures {:table-name "example_entity"}}})

(def datapotato-db
  {:schema   potato-schema
   :generate {:generator mg/generate
              #_(comment
                  ;; use clojure spec generator
                  (comp clojure.spec.gen.alpha/generate clojure.spec.alpha/gen))}
   :fixtures (merge dfn/config
                    {:get-connection (fn [_] (db-connection))
                     :setup          (fn [{{:keys [connection]} :fixtures}]
                                       ;; clear tables
                                       (comment
                                         (jdbc/execute! connection ["TRUNCATE TABLE example_entity CASCADE"]))
                                       )})})
