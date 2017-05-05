(ns ui.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [re-frisk.core :refer [enable-re-frisk!]]
            [devtools.core :as devtools]
            [ui.events.core :as events]
            [ui.subs :as subs]
            [ui.layout.core :refer [app]]))

; This flag gets turned on during development
(when goog.DEBUG
  (enable-console-print!)
  (devtools/install! [:formatters :hints :async])
  (enable-re-frisk!))

; Initialize the re-frame app
(defonce setup
  (do
    (rf/dispatch-sync [:initialize])
    (rf/clear-subscription-cache!)))

; Render the main component
(r/render
  [app]
  (.getElementById js/document "app-container"))

(comment
  (do
    (use 'figwheel-sidecar.repl-api)
    (start-figwheel!)
    (cljs-repl)))
