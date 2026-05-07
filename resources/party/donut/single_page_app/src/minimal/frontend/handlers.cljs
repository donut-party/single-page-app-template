(ns donut.minimal.frontend.handlers
  (:require
   [donut.frontend.core.utils :as dcu]
   [donut.frontend.nav.flow :as dnf]
   [donut.frontend.sync.flow :as dsf]
   [re-frame.core :as rf]))

(rf/reg-event-fx :delete-example-entity
  [rf/trim-v]
  (fn [{:keys [db]} [example-entity]]
    {:db (dcu/dissoc-entity db :example-entity (:example-entity/id example-entity))
     :fx [[:dispatch [::dsf/delete :example-entity {:route-params example-entity
                                                    :on           {:success [::dnf/navigate-route :home]}}]]]}))
