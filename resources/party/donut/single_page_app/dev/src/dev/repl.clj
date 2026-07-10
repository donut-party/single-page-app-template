(ns dev.repl
  (:require
   [clojure.tools.namespace.repl :as repl]
   [donut.system.repl :as dsr]
   [nextjournal.beholder :as beholder]))

(repl/disable-reload!)

(defonce persistent-state (atom {}))

(defn- source-file? [path]
  (re-find #"(\.cljc?|\.edn)$" (str path)))

(defn- restart*
  [path]
  (when (source-file? path)
    (try
      (dsr/restart)
      (catch Exception e
        (println "Exception reloading:")
        (println e)))))

(defn- restart [ns]
  (fn [{:keys [path]}]
    ;; NOTE this binding is load-bearing: tools.namespace's refresh
    ;; (called by dsr/restart) set!s *ns*, which throws
    ;; "Can't change/establish root binding of: *ns* with set" on
    ;; threads without a *ns* thread binding — and beholder invokes
    ;; this callback on such a thread
    (binding [*ns* ns]
      (restart* path))))

(def watcher
  (beholder/watch (restart *ns*) "src" "resources" "dev/src" "test"))

(comment
  (beholder/stop watcher))
