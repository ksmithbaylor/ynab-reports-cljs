(ns ui.pages.progress-bars
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [clojure.pprint :refer [pprint]]
            [ui.components.category-picker :refer [category-picker]]
            [ui.helpers.money :as m]
            [ui.helpers.dates :as d]))

(defn debug [label thing]
  [:pre (str label ": \n" (with-out-str (pprint thing)))])

(defn progress-bar []
  (fn [category budget transactions]
    (let [relevant-transactions (filter #(= (:entityId category)
                                            (:categoryId %))
                                        transactions)
          category-budget (->> (:monthlySubCategoryBudgets budget)
                               (filter #(= (:entityId category)
                                           (:categoryId %)))
                               first)
          spent-so-far (- (reduce m/add (map :amount relevant-transactions)))
          budgeted (:budgeted category-budget)
          total-days (d/days-in-month)
          days-so-far (d/current-day)]
      [:div
        [:h1 (:name category)]
        [:p (str "budgeted " budgeted)]
        [:p (str "spent so far " spent-so-far)]
        [:p (str "total days " total-days)]
        [:p (str "days so far " days-so-far)]
        [:hr]])))

(defn progress-bars []
  (let [budget @(rf/subscribe [:budget-this-month])
        categories @(rf/subscribe [:sub-categories])
        transactions @(rf/subscribe [:transactions-this-month])]
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
            [progress-bar category budget transactions])]))))
