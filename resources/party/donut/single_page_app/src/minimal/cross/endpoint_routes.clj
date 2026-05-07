(ns {{top/ns}}.{{main/ns}}.cross.endpoint-routes
  (:require
   ;; endpoint namespaces go here
   [{{top/ns}}.{{main/ns}}.backend.endpoint.example-entity-endpoint :as example-entity]))

(def routes
  [;; example
   ["/api/v1/example-entities"
    (merge
     {:name     :example-entities
      :ent-type :example-entity
      :id-key   :example_entity/id}
     example-entity/collection-handlers)]
   ["/api/v1/entities/{example_entity/id}"
    (merge
     {:name     :example-entity
      :ent-type :example-entity
      :id-key   :example_entity/id}
     example-entity/member-handlers)]])
