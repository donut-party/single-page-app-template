(ns donut.minimal.backend.handler
  (:require
   [reitit.ring :as rr]))

(defn wrap-db
  [handler db]
  (fn [req]
    (handler (assoc req :db db))))

(defn handler
  [{:keys [router middleware db]}]
  (-> router
      (rr/ring-handler)
      (wrap-db db)
      middleware))
