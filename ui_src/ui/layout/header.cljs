(ns ui.layout.header
  (:require [re-frame.core :as rf]
            [cljsjs.antd])
  (:require-macros [ui.helpers.antd :refer [antd->reagent]]))

(antd->reagent Spin)

(defn header []
  [:header.ui__header
    [:h1
      [:span.ynab "YNAB"]
      " "
      [:span.helper "Helper"]
      (when (and @(rf/subscribe [:loading-background])
                 (not @(rf/subscribe [:loading-total])))
        [:span.loading
          [:span.message @(rf/subscribe [:loading-message])]
          [Spin]])]])
