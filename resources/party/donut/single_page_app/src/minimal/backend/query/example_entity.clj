(ns donut.minimal.backend.query.example-entity
  "Example namespace showing how you might organize queries"
  (:require
   [honey.sql :as sql]
   [next.jdbc.sql :as jsql]))

(defn entities
  [db]
  (->> {:select [:*]
        :from   [:example_entity]}
       sql/format
       (jsql/query db)
       (into [])))

(defn entity-by-id
  [db {:keys [:example_entity/id]}]
  (->> {:select [:*]
        :from   [:example_entity]
        :where  [:= :example_entity/id id]}
       sql/format
       (jsql/query db)
       first))

(defn insert!
  [db example-entity]
  (let [result (jsql/insert! db :example_entity example-entity)]
    (jsql/query db ["SELECT * FROM example_entity WHERE rowid = ?"
                    (first (vals result))])))
