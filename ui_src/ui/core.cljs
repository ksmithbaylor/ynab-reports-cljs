(ns ui.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [re-frisk.core :refer [enable-re-frisk!]]
            [devtools.core :as devtools]
            [ui.fs.persistence :refer [read-from-disk]]
            [ui.events.core :as events]
            [ui.subs :as subs]
            [ui.layout.core :refer [app]]))

(when goog.DEBUG
  (enable-console-print!)
  (devtools/install! [:formatters :hints :async])
  (enable-re-frisk!))

(defn attach-fullscreen-handlers! []
  (let [win (.getCurrentWindow (.-remote (js/require "electron")))]
    (.on win "enter-full-screen"
      #(.add (aget js/document "body" "classList") "fullscreen"))
    (.on win "leave-full-screen"
      #(.remove (aget js/document "body" "classList") "fullscreen"))))

(defn restore-from-disk [cb]
  (read-from-disk
    (fn [err db]
      (when (some? err)
        (.error js.console err))
      (rf/dispatch-sync [:initialize db])
      (cb))))

(defn clear-preloader! []
  (let [preloader (.getElementById js/document "preloader")]
    (.add (.-classList preloader) "fulfilled")))

(defonce setup
  (do
    (attach-fullscreen-handlers!)
    (restore-from-disk
      #(do
        (rf/clear-subscription-cache!)
        (r/render
          [app]
          (.getElementById js/document "app-container"))
        (clear-preloader!)))))


(comment
  (do
    (use 'figwheel-sidecar.repl-api)
    (start-figwheel!)
    (cljs-repl)))
