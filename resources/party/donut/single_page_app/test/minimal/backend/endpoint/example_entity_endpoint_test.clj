(ns donut.minimal.backend.endpoint.example-entity-endpoint-test
  (:require
   [clojure.test :refer [deftest is use-fixtures]]
   [donut.datapotato.core :as dc]
   [donut.endpoint.test.harness :as deth]
   [donut.minimal.fixtures :as fixtures]
   [donut.system :as ds]))

(use-fixtures :each (ds/system-fixture :test))

;; example tests for a hypothetical example-entity:
(deftest gets-example-entities
  (dc/with-fixtures fixtures/datapotato-db
    (let [{:keys [ee0 ee1]} (dc/insert-fixtures {:example-entity [[2]]})]
      (is (= #{ee0 ee1}
             (set (deth/response :get :example-entities)))))))

(deftest creates-example-entity
  (is (= (deth/response :post :example-entities {} {:description "test"})
         [#:example_entity{:id          nil
                           :description "test"}])))

(deftest gets-single-example-entity
  (dc/with-fixtures fixtures/datapotato-db
    (let [{:keys [ee0]} (dc/insert-fixtures {:example-entity [[2]]})]
      (is (= ee0
             (deth/read-body (deth/handle-request :get :example-entity ee0)))))))

(deftest updates-example-entity
  (dc/with-fixtures fixtures/datapotato-db
    (let [{:keys [ee0]} (dc/insert-fixtures {:example-entity [[1]]})]
      (is (= #:example_entity{:id          (:example_entity/id ee0)
                              :description "new desc"}
             (deth/response :put :example-entity ee0 {:example_entity/description "new desc"}))))))

(deftest deletes-example-entity
  (dc/with-fixtures fixtures/datapotato-db
    (let [{:keys [ee0]} (dc/insert-fixtures {:example-entity [[1]]})]
      (deth/handle-request :delete :example-entity ee0)
      (is (= []
             (deth/response :get :example-entities))))))
