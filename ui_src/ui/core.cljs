(ns ui.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [re-frisk.core :refer [enable-re-frisk!]]))

(defn mount-root []
  (rf/clear-subscription-cache!)
  (r/render [:p "hello world"]
            (.getElementById js/document "app-container")))

(defn dev-setup []
  (when goog.DEBUG
    (enable-console-print!)
    (enable-re-frisk!)
    (println "dev mode on")))

(dev-setup)
(rf/dispatch-sync [:initialize])
(mount-root)
