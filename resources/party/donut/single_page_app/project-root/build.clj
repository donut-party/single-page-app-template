(ns build
  "donut/minimal's build script. Builds on https://github.com/seancorfield/honeysql/blob/develop/build.clj

  Run tests:
  clojure -X:test
  clojure -X:test:master
  For more information, run:
  clojure -A:deps -T:build help/doc"

  (:require
   [clojure.tools.build.api :as b])
  (:refer-clojure :exclude [test]))

(def lib 'party.donut/minimal)
(def version (format "0.0.%s" (b/git-count-revs nil)))
(def class-dir "target/classes")
(def basis (b/create-basis {:project "deps.edn"}))

(defn- pom-template [version]
  [[:description "minimal SPA with donut"]
   [:url "https://github.com/donut-power/minimal"]
   [:licenses
    [:license
     [:name "MIT"]
     [:url "https://opensource.org/license/mit/"]]]
   [:developers
    [:developer
     [:name "Daniel Higginbotham"]]]
   [:scm
    [:url "https://github.com/donut-power/minimal"]
    [:connection "scm:git:https://github.com/donut-power/minimal.git"]
    [:developerConnection "scm:git:ssh:git@github.com:donut-power/minimal.git"]
    [:tag (str "v" version)]]])

(defn- jar-opts [opts]
  (assoc opts
         :lib lib
         :version version
         :jar-file (format "target/%s-%s.jar" lib version)
         :basis basis
         :class-dir class-dir
         :target "target"
         :src-dirs ["src" "resources"]
         :pom-data (pom-template version)))

(defn jar "build a jar"
  [opts]
  (let [opts (jar-opts opts)]
    (b/delete {:path "target"})
    (b/write-pom opts)
    (b/copy-dir {:src-dirs ["src" "resources"]
                 :target-dir class-dir})
    (b/jar opts)))

;; TODO uberjar
