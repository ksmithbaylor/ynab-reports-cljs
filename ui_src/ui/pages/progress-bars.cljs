(ns ui.pages.progress-bars
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [clojure.string :refer [join]]
            [ui.components.category-picker :refer [category-picker]]
            [ui.helpers.money :as m]
            [ui.helpers.dates :as d]))

(def green "#66BB6A")
(def red "#EF5350")

(defn progress-bar-graph [title w h spent budgeted days-so-far total-days]
  (let [percent-through-month (/ days-so-far total-days)
        percent-through-budget (/ spent budgeted)
        under-budget (>= percent-through-month percent-through-budget)
        projected (/ spent percent-through-month)
        projected-percent (/ projected budgeted)
        split-x (* w percent-through-month)
        split-y (- h (* h percent-through-month))
        line-break-point (if under-budget
                           (- h (* projected-percent (- h split-y)))
                           (* (/ 1 projected-percent) split-x))
        border-intersect (if under-budget
                           (- h (* projected-percent h))
                           (* (/ 1 projected-percent) w))]
    [:div {:style {:margin "11px"}}
      [:h1 title]
      [:div {:style {:font-size 11
                     :display "flex"
                     :flex-direction "row"
                     :justify-content "space-between"}}
        [:span "B: " (m/dollars budgeted)]
        [:span "S: " (m/dollars spent)]
        [:span "P: " (m/dollars projected)]
        [:span "D: " (m/dollars (- budgeted projected))]]
      [:svg {:width w :height h}
        ; Background
        [:rect {:x 0 :y 0 :width w :height h
                :style {:fill "#F5F5F5"}}]

        (when (not= budgeted 0)
          [:g
            ; Spending shading
            (if under-budget
              [:path {:d (join " " ["M" 0 h "L" w border-intersect "L" w 0 "Z"])
                      :style {:fill green :opacity 0.15}}]
              [:path {:d (join " " ["M" 0 h "L" border-intersect 0 "L" w 0 "Z"])
                      :style {:fill "red" :opacity 0.15}}])

            ; Date shading
            (let [width split-x height (- h split-y)]
              [:rect {:x 0 :y split-y :width width :height height
                      :style {:fill "#212121"
                              :opacity 0.15}}])

            ; Spending line
            (if under-budget
              [:g
                [:line {:x1 0 :y1 h :x2 split-x :y2 line-break-point
                        :style {:stroke green
                                :stroke-width 3}}]
                [:line {:x1 split-x :y1 line-break-point :x2 w :y2 border-intersect
                        :style {:stroke green
                                :stroke-width 3
                                :stroke-dasharray "0,3,3"}}]]
              [:g
                [:line {:x1 0 :y1 h :x2 line-break-point :y2 split-y
                        :style {:stroke "red"
                                :stroke-width 3}}]
                [:line {:x1 line-break-point :y1 split-y :x2 border-intersect :y2 0
                        :style {:stroke "red"
                                :stroke-width 3
                                :stroke-dasharray "0,3,3"}}]])

            ; Static budget line
            [:line {:x1 0 :y1 h :x2 w :y2 0
                    :style {:stroke "#555"
                            :stroke-width 3}}]

            ; Dot at budget point
            [:circle {:cx split-x :cy split-y :r 4
                      :style {:fill "#555" :stroke "#555"}}]

            ; Border
            [:rect {:x 0 :y 0 :width w :height h
                    :style {:fill-opacity 0
                            :stroke "#555"
                            :stroke-width 4}}]])]]))

(defn progress-bar []
  (fn [category budget transactions]
    (let [relevant-transactions (filter #(= (:entityId category)
                                            (:categoryId %))
                                        transactions)
          category-budget (->> (:monthlySubCategoryBudgets budget)
                               (filter #(= (:entityId category)
                                           (:categoryId %)))
                               first)
          spent (if (= 0 (count relevant-transactions))
                  0
                  (- (reduce m/add (map :amount relevant-transactions))))
          budgeted (:budgeted category-budget)
          total-days (d/days-in-month)
          days-so-far (d/current-day)]
      [progress-bar-graph (:name category) 250 250 spent budgeted days-so-far total-days])))

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
          [:div {:style {:display "flex"
                         :flex-wrap "wrap"}}
            ; [progress-bar-graph "Under"     250 250 45  120 20 30]
            ; [progress-bar-graph "Over"      250 250 95  120 20 30]
            ; [progress-bar-graph "Way Under" 250 250 3   120 20 30]
            ; [progress-bar-graph "Way Over"  250 250 300 120 20 30]
            ; [progress-bar-graph "Exact"     250 250 60  120 20 30]
            (for [category selected-categories]
              ^{:key (:entityId category)}
              [progress-bar category budget transactions])]]))))
