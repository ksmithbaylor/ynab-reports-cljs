(ns ui.core
  (:require [reagent.core :as r]
            [cljs.pprint :refer [pprint]]))

(defonce todos (r/atom (sorted-map)))
(defonce next-id (r/atom 0))
(defonce input-value (r/atom ""))

(defn clear-all-todos []
  (reset! todos (sorted-map)))
(defn clear-text-field []
  (reset! input-value ""))

(defn add-todo []
  (let [id (swap! next-id inc)
        text @input-value]
    (when-not (empty? text)
      (clear-text-field)
      (swap! todos assoc id {:id id
                             :text text
                             :done false}))))

(defn remove-todo [id]
  (swap! todos dissoc id))

(defn toggle-todo [id]
  (let [before (get @todos id)
        after (assoc before :done (not (:done before)))]
    (swap! todos assoc id after)))

(defn todo-item [{:keys [id text done]}]
  [:li {:on-click #(toggle-todo id) :key id}
    [:span {:style {:text-decoration (if done "line-through" "inherit")
                    :cursor "pointer"}}
      text]])

(defn todo-list []
  [:ul (map todo-item (vals @todos))])

(defn input-box []
  [:input {:type "text"
           :value @input-value
           :on-change #(reset! input-value (-> % .-target .-value))
           :on-blur clear-text-field
           :on-key-down #(case (.-which %)
                           13 (add-todo)
                           27 (reset! input-value "")
                           nil)}])

(defn app []
  [:div
    [input-box]
    [todo-list]
    [:button {:on-click clear-all-todos}
      "Clear all"]])

(r/render
  [app]
  (js/document.getElementById "app-container"))
