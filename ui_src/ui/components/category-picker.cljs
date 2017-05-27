(ns ui.components.category-picker
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [cljsjs.antd])
  (:require-macros [ui.util.antd :refer [antd->reagent]]))

(antd->reagent TreeSelect TreeSelect.TreeNode)

(defn category-picker [{:keys [onChange]}]
  (let [master-categories @(rf/subscribe [:categories])
        outflow-categories (filter #(and (= "OUTFLOW" (:type %))
                                         (:deleteable %))
                                   master-categories)
        selected (r/atom #js [])]
    (fn []
      (let [num-selected (.-length @selected)
            main-label (str "Categories (" num-selected ")")]
        [:div.category-picker
          [TreeSelect {:treeCheckable true
                       :treeDefaultExpandAll true
                       :placeholder main-label
                       :style {:width "100px"}
                       :dropdownMatchSelectWidth false
                       :dropdownStyle {:maxHeight "300px" :width "300px"}
                       :maxTagTextLength 0
                       :value @selected
                       :onChange (fn [value label extra]
                                   (reset! selected value)
                                   (when onChange
                                     (onChange value label extra)))
                       :multiple true}
            (for [master-category outflow-categories]
              [TreeSelect-TreeNode {:title (:name master-category)
                                    :key (:entityId master-category)
                                    :value (:entityId master-category)
                                    :selectable false}
                (for [sub-category (:subCategories master-category)]
                  [TreeSelect-TreeNode {:title (:name sub-category)
                                        :key (:entityId sub-category)
                                        :value (:entityId sub-category)
                                        :selectable false
                                        :isLeaf true}])])]]))))
