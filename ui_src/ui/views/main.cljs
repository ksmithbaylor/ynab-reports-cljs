(ns ui.views.main
  (:require [re-frame.core :as rf]
            [ui.pages.hello :refer [hello]]
            [ui.pages.other :refer [other]]
            [ui.pages.preferences :refer [preferences]]))

(def routes
  {:hello hello
   :other other
   :preferences preferences})

(defn main []
  (let [page @(rf/subscribe [:page])
        component (routes page)]
    [:main {:class "ui__main"}
      [component]]))
