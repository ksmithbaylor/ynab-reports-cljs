(ns ui.layout.main
  (:require [re-frame.core :as rf]
            [ui.pages.summary :refer [summary]]
            [ui.pages.preferences :refer [preferences]]
            [ui.pages.progress-bars :refer [progress-bars]]))

(def routes
  {:summary [#'summary]
   :preferences [#'preferences]
   :progress-bars [#'progress-bars]})

(defn main []
  (when-not @(rf/subscribe [:loading-total])
    [:main.ui__main
      (routes @(rf/subscribe [:page]))]))
