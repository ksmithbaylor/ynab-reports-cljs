(ns ui.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [re-frisk.core :refer [enable-re-frisk!]]
            [ui.events :as events]
            [ui.subs :as subs]
            [ui.views.core :refer [app]]))

; This flag gets turned on during development
(when goog.DEBUG
  (enable-console-print!)
  (enable-re-frisk!)
  (println "dev mode on"))

; Initialize the re-frame app
(rf/dispatch-sync [:initialize])
(rf/clear-subscription-cache!)

; Render the main component
(r/render
  [app]
  (.getElementById js/document "app-container"))
