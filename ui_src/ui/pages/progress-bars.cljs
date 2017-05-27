(ns ui.pages.progress-bars
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [clojure.pprint :refer [pprint]]
            [ui.components.category-picker :refer [category-picker]]))

(defn debug [label thing]
  [:pre (str label ": \n" (with-out-str (pprint thing)))])

(defn progress-bar []
  (fn [category budget]
    [:div
      [:p (str "progress bar for " (:name category))]]))

(defn progress-bars []
  (let [budget @(rf/subscribe [:budget-this-month])
        categories @(rf/subscribe [:sub-categories])]
    (fn []
      (let [selected-category-ids @(rf/subscribe [:progress-bars/selected-category-ids])
            selected-categories (filter #(get selected-category-ids
                                              (:entityId %))
                                        categories)]
        [:div
          [category-picker
           {:selected selected-category-ids
            :onChange #(rf/dispatch [:progress-bars/set-selected-category-ids
                                     (set (js->clj %))])}]
          (for [category selected-categories]
            ^{:key (:entityId category)}
            [progress-bar category budget])]))))
