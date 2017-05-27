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
      [:p "progress bar!"]
      (debug "progress-bar -> category" category)]))

(defn progress-bars []
  (let [budget @(rf/subscribe [:budget-this-month])
        categories @(rf/subscribe [:sub-categories])
        selected-category-ids (r/atom [])]
    (fn []
      (let [selected-categories (doall
                                  (filter #(get
                                             (set @selected-category-ids)
                                             (:entityId %))
                                          categories))]
        [:div
          [category-picker {:onChange #(reset! selected-category-ids (js->clj %))}]
          (for [category selected-categories]
            ^{:key (:entityId category)}
            [progress-bar category budget])]))))
