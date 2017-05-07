(ns ui.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [re-frisk.core :refer [enable-re-frisk!]]
            [devtools.core :as devtools]
            [ui.events.core :as events]
            [ui.subs :as subs]
            [ui.layout.core :refer [app]]))

(when goog.DEBUG
  (enable-console-print!)
  (devtools/install! [:formatters :hints :async])
  (enable-re-frisk!))

(defonce setup
  (do
    (let [win (.getCurrentWindow (.-remote (js/require "electron")))]
      (.on win "enter-full-screen"
        #(.add (aget js/document "body" "classList") "fullscreen"))
      (.on win "leave-full-screen"
        #(.remove (aget js/document "body" "classList") "fullscreen")))
    (rf/dispatch-sync [:initialize])
    (rf/clear-subscription-cache!)))

(r/render
  [app]
  (.getElementById js/document "app-container"))

(let [preloader (.getElementById js/document "preloader")]
  (.add (.-classList preloader) "fulfilled"))

(comment
  (do
    (use 'figwheel-sidecar.repl-api)
    (start-figwheel!)
    (cljs-repl)))
