(ns ui.views
  (:require [re-frame.core :as rf]))

(defn main []
  [:div
    [:p @(rf/subscribe [:text])]
    [:button {:onClick #(rf/dispatch [:append "!"])}
      "get excited!"]])
