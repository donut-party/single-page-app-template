(ns update
  (:require
   [babashka.fs :as fs]
   [babashka.process :as ps]
   [clojure.string :as str]
   [util]))

(def project-root (util/project-root))
(def template-dir (fs/path project-root "resources/party/donut/single_page_app"))
(def source-root  (fs/path (fs/parent project-root) "minimal"))
(def source-files
  (->> (ps/sh {:dir source-root} "git ls-files")
       :out
       str/split-lines))

(defn -main
  []
  ;; remove all files from template_dir
  ;; move all files to template-dir/root
  ;; then move src/donut/minimal to template_dir/src
  ;; same with test/donut/minimal
  ;; then in template_dir, replace donut.minimal with {{top/ns}}.{{main/ns}}
  ;; then copy template.edn in

  (fs/delete-tree template-dir)

  ;; necessary for how clj-new works; makes it easier to copy all files from the
  ;; project root
  (fs/create-dirs (fs/path template-dir "project-root"))

  ;; copies source files in git
  (doseq [source-file (remove #(re-find #"(src|test)/donut/minimal" %) source-files)]
    (if-let [parent (fs/parent source-file)]
      (do
        (fs/create-dirs (fs/path template-dir parent))
        (fs/copy (fs/path source-root source-file) (fs/path template-dir source-file)))
      (fs/copy (fs/path source-root source-file) (fs/path template-dir "project-root" source-file))))

  ;; these go in their own directory to make it easier for clj-new to put them
  ;; in "top/file / main/file"
  (fs/copy-tree (fs/path source-root "src/donut/minimal") (fs/path template-dir "src-app"))
  (fs/copy-tree (fs/path source-root "test/donut/minimal") (fs/path template-dir "test-app"))

  ;; put template interpolation in place
  (doseq [path (fs/glob template-dir "**/**")]
    (when (fs/regular-file? path)
      (let [path-str (str path)]
        (spit path-str
              (-> path-str
                  slurp
                  (str/replace #"donut\.minimal" "{{top/ns}}.{{main/ns}}"))))))

  ;; template.edn is stored in this project's root so it doesn't get blown away
  ;; when this script is run
  (fs/copy (fs/path project-root "template.edn")
           (fs/path template-dir "template.edn")))
