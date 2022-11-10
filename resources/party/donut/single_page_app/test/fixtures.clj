(ns {{top/ns}}.fixtures
  (:require
   [donut.datapotato.next-jdbc :as dfn]
   [donut.endpoint.test.harness :as deth]
   [{{top/ns}}.backend.system] ;; for multimethod
   [next.jdbc :as jdbc]))

(defn db-connection
  []
  (deth/instance [:db :connection]))

(def potato-schema
  {:example-entity {:prefix   :ee
                    :generate {:schema :example-entity/record}
                    :fixtures {:table-name "example_entity"}}})

(def datapotato-db
  {:schema   potato-schema
   :generate {:generator nil
              ;; example values for :generator include:
              #_(comment
                  ;; malli
                  malli.generator/generate

                  ;; clojure spec
                  (comp clojure.spec.gen.alpha/generate clojure.spec.alpha/gen))}
   :fixtures (merge dfn/config
                    {:get-connection (fn [_] (db-connection))
                     :setup          (fn [{{:keys [connection]} :fixtures}]
                                       ;; clear tables
                                       (comment
                                         (jdbc/execute! connection ["TRUNCATE TABLE example_entity CASCADE"]))
                                       )})})
