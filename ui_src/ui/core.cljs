(ns ui.core
  (:require [re-frame.core :as rf]
            [re-frisk.core :refer [enable-re-frisk!]]
            [devtools.core :as devtools]
            [ui.fs.persistence :refer [read-from-disk]]
            [ui.events.core :as events]
            [ui.subs :as subs]))

(defn setup-debugging! []
  (when goog.DEBUG
    (enable-console-print!)
    (devtools/install! [:formatters :hints :async])
    (enable-re-frisk!)))

(defn print-bar! []
  (js/console.log
    (str "%c" (apply str (repeat 78 " ")))
    "background-color: #13323c"))

(defn boot-application! []
  (read-from-disk
    (fn [err db]
      (when (some? err)
        (.error js.console err))
      (if db
        (rf/dispatch [:deeplink db])
        (rf/dispatch [:initialize])))))

(defonce setup
  (do
    (setup-debugging!)
    (print-bar!)
    (boot-application!)
    nil))
