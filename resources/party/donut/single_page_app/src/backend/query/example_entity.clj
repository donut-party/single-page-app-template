(ns donut.minimal.backend.query.example-entity
  "Example namespace showing how you might organize queries"
  (:require
   [honey.sql :as sql]
   [next.jdbc.sql :as jsql]))

(defn entities
  [db]
  (->> {:select [:*]
        :from   [:entity]}
       sql/format
       (jsql/query db)
       (into [])))

(defn entity-by-id
  [db {:keys [:entity/id]}]
  (->> {:select [:*]
        :from   [:entity]
        :where  [:= :entity/id id]}
       sql/format
       (jsql/query db)
       first))
