(ns ui.layout.main
  (:require [re-frame.core :as rf]
            [ui.pages.summary :refer [summary]]
            [ui.pages.preferences :refer [preferences]]))

(def routes
  {:summary summary
   :preferences preferences})

(defn main []
  (let [page @(rf/subscribe [:page])
        component (routes page)]
    [:main.ui__main
      [component]]))
