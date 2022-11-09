(ns donut.minimal.cross.endpoint-routes
  (:require [donut.routes :as dr]
            #?@(:clj
                [;; endpoint namespaces go here
                 ])))

(def routes
  (dr/merge-route-opts
   [;; example
    #_["/api/v1/entities"
       {:name     :entities
        :ent-type :entity
        :id-key   :entity/id}
       #?(:clj entity/collection-handlers)]
    #_["/api/v1/entities/{entity/id}"
       {:name     :entity
        :ent-type :entity
        :id-key   :entity/id}
       #?(:clj entity/member-handlers)]]))
