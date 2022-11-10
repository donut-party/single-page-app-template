(ns {{top/ns}}.frontend.frontend-routes
  (:require
   [reitit.coercion.malli :as rm]
   [donut.frontend.form.flow :as dff]
   [donut.frontend.sync.flow :as dsf]
   [{{top/ns}}.frontend.components.home :as h]
   [{{top/ns}}.frontend.components.example-entity :as ee]))

(def routes
  [["/"
    {:name       :home
     :components {:main [h/component]}
     :lifecycle  {:enter [[::dsf/get :example-entities]]}}]
   ["/example-entities/{example_entity/id}"
    {:name       :example-entity
     :components {:main [ee/show]}
     :coercion   rm/coercion
     :lifecycle  {:enter [[::dff/clear {:donut.form/key [:post :example-entity]}]
                          [::dsf/get :example-entity {::dsf/rules #{::dsf/merge-route-params}}]]}
     :parameters {:path [:map [:example-entity/id int?]]}}]])
