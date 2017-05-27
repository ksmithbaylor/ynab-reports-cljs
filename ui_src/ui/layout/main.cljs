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
  (let [page @(rf/subscribe [:page])
        component (routes page)]
    [:main.ui__main component]))
