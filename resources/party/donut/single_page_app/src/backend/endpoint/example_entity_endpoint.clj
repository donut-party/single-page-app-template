(ns {{top/ns}}.backend.endpoint.example-entity-endpoint
  (:require
   [{{top/ns}}.backend.query.example-entity :as qe]
   [next.jdbc.sql :as jsql]))

(def collection-handlers
  {:get
   {:handler
    (fn [{:keys [db]}]
      {:status 200
       :body   (qe/entities db)})}

   :post
   {:handler
    (fn [{:keys [all-params db]}]
      {:status 200
       :body   (jsql/insert! db :entity all-params)})}})

(def member-parameters
  {:path [:map
          [:example_entity/id int?]]})

(def member-handlers
  {:get
   {:parameters member-parameters
    :handler    (fn [{:keys [all-params db]}]
                  {:status 200
                   :body   (qe/entity-by-id db all-params)})}

   :put
   {:parameters member-parameters
    :handler    (fn [{:keys [all-params db]}]
                  (jsql/update! db
                                :entity
                                (dissoc all-params :entity/id)
                                (select-keys all-params [:entity/id]))
                  {:status 200
                   :body   (qe/entity-by-id db all-params)})}

   :delete
   {:parameters member-parameters
    :handler    (fn [{:keys [all-params db]}]
                  (jsql/delete! db :entity (select-keys all-params [:entity/id])))}})
