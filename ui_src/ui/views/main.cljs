(ns ui.views.main
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [cljsjs.antd])
  (:require-macros [ui.helpers.antd :refer [antd->reagent]]))

(antd->reagent Button)

(defn main []
  [:main {:class "ui__main"}
    [:p @(rf/subscribe [:text])]
    [Button {:type "primary"
             :onClick #(rf/dispatch [:append "!"])}
      "Get excited!"]])
