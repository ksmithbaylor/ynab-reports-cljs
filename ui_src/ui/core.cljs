(ns ui.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [re-frisk.core :refer [enable-re-frisk!]]
            [ui.events :as events]
            [ui.subs :as subs]
            [ui.views :as views]))

(defn dev-setup []
  (when goog.DEBUG
    (enable-console-print!)
    (enable-re-frisk!)
    (println "dev mode on")))

(defn init []
  (dev-setup)
  (rf/dispatch-sync [:initialize])
  (rf/clear-subscription-cache!)
  (r/render
    [views/main]
    (.getElementById js/document "app-container")))

(init)
