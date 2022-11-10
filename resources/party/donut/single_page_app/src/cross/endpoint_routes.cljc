(ns {{top/ns}}.cross.endpoint-routes
  (:require [donut.routes :as dr]
            #?@(:clj
                [;; endpoint namespaces go here
                 [{{top/ns}}.backend.endpoint.example-entity-endpoint :as example-entity]])))

(def routes
  (dr/merge-route-opts
   [;; example
    ["/api/v1/example-entities"
     {:name     :example-entities
      :ent-type :example-entity
      :id-key   :example_entity/id}
     #?(:clj example-entity/collection-handlers)]
    ["/api/v1/entities/{entity/id}"
     {:name     :example-entity
      :ent-type :example-entity
      :id-key   :example_entity/id}
     #?(:clj example-entity/member-handlers)]]))
