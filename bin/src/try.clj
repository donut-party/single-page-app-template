(ns try
  (:require
   [babashka.fs :as fs]
   [babashka.process :as ps]
   [clojure.string :as str]
   [util]))

(def project-root (util/project-root))
(def template-test-dir (fs/path (fs/parent project-root) "template-test"))


(defn -main
  []
  (fs/delete-tree template-test-dir)
  (ps/shell
   {:dir project-root}
   (str/join
    " "
    ["clojure"
     "-Sdeps '{:deps {party.donut/single-page-app {:local/root \"./\"}}}'"
     "-Tnew create"
     ":template party.donut/single-page-app"
     ":name donute-template-test/test-app"
     ":target-dir ../template-test"])))
