(ns ui.components.loading-indicator
  (:require [cljsjs.antd])
  (:require-macros [ui.helpers.antd :refer [antd->reagent]]))

(antd->reagent Spin)

(defn loading-indicator []
  [:div {:style {:position "absolute"
                 :top 0
                 :left 0
                 :height "100vh"
                 :width "100%"
                 :display "flex"
                 :align-items "center"
                 :justify-content "center"
                 :background-color "white"
                 :opacity 0.7
                 :z-index 999}}
    [Spin {:size "large" :font-size "2em"}]])
