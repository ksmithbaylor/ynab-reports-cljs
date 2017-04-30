(ns ui.views.sidebar
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [cljsjs.antd])
  (:require-macros [ui.helpers.antd :refer [antd->reagent]]))

(antd->reagent Menu
               Menu.Item)

(defn sidebar []
  [:aside {:class "ui__sidebar"}
    [Menu {:theme "dark"
           :mode "inline"
           :selectedKeys [(name @(rf/subscribe [:page]))]
           :onClick #(rf/dispatch [:navigate (keyword (.-key %1))])}
      [Menu-Item {:key :hello} "Hello"]
      [Menu-Item {:key :other} "Other"]]])
