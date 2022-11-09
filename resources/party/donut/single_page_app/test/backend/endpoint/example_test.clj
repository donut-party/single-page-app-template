(ns donut.minimal.backend.endpoint.example-test
  (:require
   [clojure.test :refer [deftest is use-fixtures]]
   [donut.datapotato.core :as dc]
   [donut.endpoint.test.harness :as deth]
   [donut.minimal.data :as data]))

(use-fixtures :each (deth/system-fixture [:test]))

(comment
  ;; example tests for a hypothetical example-entity:
  (deftest gets-example-entities
    (dc/with-fixtures data/datapotato-db
      (let [{:keys [u0 u1]} (dc/insert-fixtures {:example-entity [[2]]})]
        (is (= [u0 u1]
               (deth/read-body (deth/handle-request :get :example-entities)))))))

  (deftest creates-example-entity
    (is (deth/contains-entity? (deth/read-body (deth/handle-request :post :example-entities {:example-entity-name "test"}))
                               #:example-entity{:example-entityname "test"})))

  (deftest gets-single-example-entity
    (dc/with-fixtures data/datapotato-db
      (let [{:keys [u0]} (dc/insert-fixtures {:example-entity [[2]]})]
        (is (= [[:entities [:example-entity :example-entity/id [u0]]]]
               (deth/read-body (deth/handle-request :get [:example-entity (select-keys u0 [:example-entity/id])])))))))

  (deftest updates-example-entity
    (dc/with-fixtures data/datapotato-db
      (let [{:keys [u0]} (dc/insert-fixtures {:example-entity [[1]]})]
        (is (= #:example-entity{:id       (:example-entity/id u0)
                                :example-entityname "new-example-entity-name"}
               (-> (deth/handle-request
                    :put
                    [:example-entity (select-keys u0 [:example-entity/id])]
                    {:example-entityname "new-example-entity-name"})
                   (deth/read-body)))))))

  (deftest deletes-example-entity
    (dc/with-fixtures data/datapotato-db
      (let [{:keys [u0]} (dc/insert-fixtures {:example-entity [[1]]})]
        (deth/handle-request :delete [:example-entity (select-keys u0 [:example-entity/id])])
        (is (= []
               (deth/read-body (deth/handle-request :get :example-entities))))))))
