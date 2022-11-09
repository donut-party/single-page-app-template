(ns donut.minimal.frontend.subs
  (:require
   [donut.frontend.core.utils :as dcu]
   [donut.frontend.nav.utils :as dnu]
   [re-frame.core :as rf]))

(rf/reg-sub :example-entities
  (fn [db]
    (dcu/entities db :entity :example_entity/id)))

(rf/reg-sub :routed-example-entity
  (fn [db]
    (dnu/routed-entity db :entity :example_entity/id)))
