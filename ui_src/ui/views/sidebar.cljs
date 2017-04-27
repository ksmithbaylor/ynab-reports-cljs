(ns ui.views.sidebar
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [cljsjs.antd])
  (:require-macros [ui.helpers.antd :refer [antd->reagent]]))

(antd->reagent Menu
               Menu.SubMenu
               Menu.Item)

(defn sidebar []
  [:aside {:class "ui__sidebar"}
    [Menu {:theme "dark" :mode "inline"}
      [Menu-SubMenu {:key "sub1" :title "Things"}
        [Menu-Item {:key "1"} "One"]
        [Menu-Item {:key "2"} "Two"]
        [Menu-Item {:key "3"} "Three"]]]])
