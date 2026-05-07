#! /usr/bin/env bb

(require
 '[babashka.fs :as fs]
 '[babashka.process :as ps]
 '[clojure.string :as str])

(def project-root (str/trim (:out (ps/sh "git rev-parse --show-toplevel"))))
(def template-dir (format "%s/resources/party/donut/single_page_app" project-root))
(def source-root (format "%s/../minimal" project-root))
(def source-files
  (->> (ps/sh {:dir source-root} "git ls-files")
       :out
       str/split-lines))

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

(fs/copy-tree (fs/path source-root "src/donut/minimal") (fs/path template-dir "src"))
(fs/copy-tree (fs/path source-root "test/donut/minimal") (fs/path template-dir "test"))

(doseq [path (fs/glob template-dir "**/**")]
  (when (fs/regular-file? path)
    (let [path-str (str path)]
      (spit path-str
            (-> path-str
                slurp
                (str/replace #"donut\.minimal" "{{top/ns}}.{{main/ns}}"))))))

(fs/copy (fs/path project-root "template.edn")
         (fs/path template-dir "template.edn"))
