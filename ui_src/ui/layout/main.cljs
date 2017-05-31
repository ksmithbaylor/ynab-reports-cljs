(ns ui.layout.main
  (:require [re-frame.core :as rf]
            [ui.pages.preferences :refer [preferences]]
            [ui.pages.category-projections :refer [category-projections]]))

(def routes
  {:preferences [#'preferences]
   :category-projections [#'category-projections]})

(defn main []
  (when-not @(rf/subscribe [:loading-total])
    [:main.ui__main
      (routes @(rf/subscribe [:page]))]))
