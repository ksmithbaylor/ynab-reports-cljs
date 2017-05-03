(ns ui.components.buttons
  (:require [cljsjs.antd]
            [re-frame.core :as rf])
  (:require-macros [ui.helpers.antd :refer [antd->reagent]]))

(antd->reagent Button)

(defn excited-button []
  [Button {:type "primary"
           :onClick #(rf/dispatch [:append "!"])}
    "Get excited!"])
