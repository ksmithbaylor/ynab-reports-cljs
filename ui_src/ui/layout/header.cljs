(ns ui.layout.header
  (:require [re-frame.core :as rf]
            [cljsjs.antd])
  (:require-macros [ui.util.antd :refer [antd->reagent]]))

(antd->reagent Spin)

(defn header []
  (let [loading @(rf/subscribe [:loading-background])
        loading-message @(rf/subscribe [:loading-message])]
    [:header.ui__header
      [:h1
        [:span.ynab "YNAB"]
        " "
        [:span.helper "Helper"]
        (when loading
          [:span.loading
            [:span.message loading-message]
            [Spin]])]]))
