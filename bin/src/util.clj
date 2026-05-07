(ns util 
  (:require
   [babashka.fs :as fs]
   [babashka.process :as ps]
   [clojure.string :as str]))

(defn project-root
  []
  (fs/path (str/trim (:out (ps/sh "git rev-parse --show-toplevel")))))
