(ns update
  "Rebuilds the clj-new template by copying from the \"minimal\" example SPA"
  (:require
   [babashka.fs :as fs]
   [babashka.process :as ps]
   [clojure.string :as str]
   [util]))

(def project-root
  "path for this repo"
  (util/project-root))

(def template-dir
  "where files for the clj-new template go"
  (fs/path project-root "resources/party/donut/single_page_app"))

(def template-project-root-dir
  "this directory holds files that are meant to end up in the project root after a
  project is created with clj-new. They need to be placed in a sub-directory
  like this because of how clj-new's templating/file copying works"
  (fs/path template-dir "project-root"))

(def ingredients-dir
  "files to copy into the clj-new template under project-root. these aren't
  derived from the minimal app"
  (fs/path project-root "resources/party/donut/ingredients"))

(def source-root
  "has the bulk of the files we copy into the template"
  (fs/path (fs/parent project-root) "minimal"))

(defn non-app-source-file-paths
  "Gets relative paths to all non-app files (e.g. package.json, deps.edn) stored
  in git"
  []
  (->> (ps/sh {:dir source-root} "git ls-files")
       :out
       str/split-lines
       (remove #(re-find #"(src|test)/donut/minimal" %))))

(defn -main
  []
  ;; move all files to template-dir/root
  ;; then move src/donut/minimal to template_dir/src
  ;; same with test/donut/minimal
  ;; then in template_dir, replace donut.minimal with {{top/ns}}.{{main/ns}}
  ;; then copy template.edn in

  ;; remove all files from template_dir to start fresh; if we only copied files
  ;; over we'd retain files we don't want
  (fs/delete-tree template-dir)

  (fs/create-dirs template-project-root-dir)

  ;; copies non-app source files in git
  (doseq [source-file-path (non-app-source-file-paths)]
    (if-let [parent (fs/parent source-file-path)]
      (do
        (fs/create-dirs (fs/path template-dir parent))
        (fs/copy (fs/path source-root source-file-path) (fs/path template-dir source-file-path)))
      (fs/copy (fs/path source-root source-file-path) (fs/path template-project-root-dir source-file-path))))

  (fs/copy-tree ingredients-dir template-project-root-dir {:replace-existing true})
  
  ;; these go in their own directory to make it easier for clj-new to put them
  ;; in "top/file / main/file"
  (fs/copy-tree (fs/path source-root "src/donut/minimal") (fs/path template-dir "src-app"))
  (fs/copy-tree (fs/path source-root "test/donut/minimal") (fs/path template-dir "test-app"))

  (fs/copy (fs/path project-root "resources/template.edn") (fs/path template-dir "template.edn"))
  
  ;; put template interpolation in place
  (doseq [path (fs/glob template-dir "**/**")]
    (when (fs/regular-file? path)
      (let [path-str (str path)]
        (spit path-str
              (-> path-str
                  slurp
                  (str/replace #"donut\.minimal" "{{top/ns}}.{{main/ns}}")
                  (str/replace #"donut/minimal" "{{top/ns}}/{{main/ns}}")))))))
