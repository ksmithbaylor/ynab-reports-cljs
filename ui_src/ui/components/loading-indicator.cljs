(ns ui.components.loading-indicator
  (:require [re-frame.core :as rf]
            [cljsjs.antd])
  (:require-macros [ui.helpers.antd :refer [antd->reagent]]))

(antd->reagent Spin)

(defn loading-indicator []
  [:div {:class (if @(rf/subscribe [:loading-total])
                  "preloader half"
                  "preloader fulfilled")
         :style {:position "absolute"
                 :top 0
                 :left 0
                 :height "100vh"
                 :width "100%"
                 :display "flex"
                 :flex-direction "column"
                 :align-items "center"
                 :justify-content "center"
                 :background-color "white"}}
    [Spin {:size "large" :font-size "2em"}]
    [:p {:style {:margin "1em"}}
      @(rf/subscribe [:loading-message])]])
