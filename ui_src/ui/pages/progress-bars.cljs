(ns ui.pages.progress-bars
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [ui.components.category-picker :refer [category-picker]]
            [ui.helpers.money :as m]
            [ui.helpers.dates :as d]))

(defn progress-bar-graph [w h spent budgeted days-so-far total-days]
  (let [percent-through-month (/ days-so-far total-days)
        percent-through-budget (/ spent budgeted)
        under-budget (> percent-through-month percent-through-budget)
        projected (/ spent percent-through-month)
        projected-percent (/ projected budgeted)
        split-x (* w percent-through-month)
        split-y (- h (* (/ 2 3) h percent-through-budget))
        projected-y (- h (* (/ 2 3) h projected-percent))
        budget-y (- h (* (/ 2 3) h percent-through-month))
        color (if under-budget "green" "red")]
    [:svg {:width w :height h}
      ; Border
      [:rect {:x 0 :y 0 :width w :height h
              :style {:fill "#eee"
                      :stroke "#555"
                      :stroke-width 3}}]

      (when (not= budgeted 0)
        [:g
          ; Static budget line
          [:line {:x1 0 :y1 h :x2 w :y2 (/ h 3)
                  :style {:stroke "black"
                          :stroke-width 3}}]

          ; Date progress line
          [:line {:x1 split-x :y1 0 :x2 split-x :y2 h
                  :style {:stroke "black"
                          :stroke-width 1}}]

          ; Spending line (so far)
          [:line {:x1 0 :y1 h :x2 split-x :y2 split-y
                  :style {:stroke color
                          :stroke-width 2}}]

          ; Spending line (projected)
          [:line {:x1 split-x :y1 split-y :x2 w :y2 projected-y
                  :style {:stroke color
                          :stroke-width 2
                          :stroke-dasharray "3,3"}}]

          ; Dot at budget point
          [:circle {:cx split-x :cy budget-y :r 4
                    :style {:fill "black" :stroke "black"}}]

          ; Dot at split
          [:circle {:cx split-x :cy split-y :r 4
                    :style {:fill color :stroke color}}]])

      ; Summary text
      (let [size 16 left 5 spacing "1.2em"
            word (if under-budget " ahead" " behind")
            tspan (fn [& s] [:tspan {:x left :dy spacing} (apply str s)])]
        [:text {:x left :y 0 :font-size size}
          (tspan (m/dollars budgeted)
                 " budgeted")
          (tspan (m/dollars spent)
                 " spent")
          (tspan (m/dollars projected)
                 " projected")
          (tspan (m/dollars (js/Math.abs (- spent (* budgeted percent-through-month))))
                 word " now")
          (tspan (m/dollars (js/Math.abs (- projected budgeted)))
                 word " by EOM")])]))

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
      [:div {:style {:margin "10px"}}
        [:h1 (:name category)]
        [progress-bar-graph 250 250 spent budgeted days-so-far total-days]])))

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
            (for [category selected-categories]
              ^{:key (:entityId category)}
              [progress-bar category budget transactions])]]))))
