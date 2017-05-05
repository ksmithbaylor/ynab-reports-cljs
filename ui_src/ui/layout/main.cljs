(ns ui.layout.main
  (:require [re-frame.core :as rf]
            [ui.pages.preferences :refer [preferences]]))

(def routes
  {:preferences preferences})

(defn main []
  (let [page @(rf/subscribe [:page])
        component (routes page)]
    [:main {:class "ui__main"}
      [component]]))
