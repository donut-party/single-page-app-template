(ns donut.minimal.backend.endpoint.example-entity-endpoint
  (:require
   [donut.minimal.backend.query.example-entity :as qe]
   [next.jdbc.sql :as jsql]))

(def collection-parameters
  {:path [:map [:todo_list/id int?]]})

(def collection-handlers
  {:get
   {:parameters collection-parameters
    :handler
    (fn [{:keys [db]}]
      {:status 200
       :body   (qe/entities db)})}

   :post
   {:parameters collection-parameters
    :handler
    (fn [{:keys [all-params db]}]
      {:status 200
       :body   (jsql/insert! db :entity all-params)})}})

(def member-parameters
  {:path [:map
          [:entity/id int?]]})

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
