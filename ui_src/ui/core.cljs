(ns ui.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [re-frisk.core :refer [enable-re-frisk!]]
            ; [devtools.core :as devtools]
            [dirac.runtime :as dirac]
            [ui.events :as events]
            [ui.subs :as subs]
            [ui.views.core :refer [app]]))

; This flag gets turned on during development
(when goog.DEBUG
  (enable-console-print!)
  ; (devtools/install!)
  (dirac/install!)
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
