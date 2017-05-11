(ns ui.core
  (:require [re-frame.core :as rf]
            [re-frisk.core :refer [enable-re-frisk!]]
            [devtools.core :as devtools]
            [ui.fs.persistence :refer [read-from-disk]]
            [ui.events.core :as events]
            [ui.subs :as subs]))

(defonce setup
  (do
    (when goog.DEBUG
      (enable-console-print!)
      (devtools/install! [:formatters :hints :async])
      (enable-re-frisk!))
    (read-from-disk
      (fn [err db]
        (when (some? err)
          (.error js.console err))
        (if db
          (rf/dispatch [:deeplink db])
          (rf/dispatch [:initialize]))))))

(comment
  (do
    (use 'figwheel-sidecar.repl-api)
    (start-figwheel!)
    (cljs-repl)))
