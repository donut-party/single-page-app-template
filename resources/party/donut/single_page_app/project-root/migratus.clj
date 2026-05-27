(ns migratus
  (:require
   #_:clj-kondo/ignore
   [{{top/ns}}.{{main/ns}}.backend.system :as system]
   [donut.system :as ds]))

(defn migratus-config
  [profile-name]
  (let [system (ds/start (ds/system profile-name
                           {:db {:migratus {::ds/start (fn [{:keys [::ds/config]}] config)}}}
                           #{[:db :migratus]}))]
    (ds/instance system [:db :migratus])))

(defn get-profile-name
  []
  (or (some-> (System/getenv "DONUT_PROFILE_NAME") keyword)
      :dev))

(migratus-config (get-profile-name))
