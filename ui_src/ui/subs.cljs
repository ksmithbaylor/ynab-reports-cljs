(ns ui.subs
  (:require [re-frame.core :as rf]
            [ui.db :refer [initial-state]]
            [ui.budget.core :as b]
            [ui.helpers.dates :as d]))

(rf/reg-sub :page                  #(get-in % [:page]))

(rf/reg-sub :loading-message       #(get-in % [:loading :message]))
(rf/reg-sub :loading-background    #(get-in % [:loading :background]))
(rf/reg-sub :loading-total         #(get-in % [:loading :total]))
(rf/reg-sub :loading-page
  (fn [db [_ page]]
    (get-in db [:loading :single-page page])))

(rf/reg-sub :budget-location       #(get-in % [:budget :file :location]))
(rf/reg-sub :budget-yfull-file     #(get-in % [:budget :file :yfull]))
(rf/reg-sub :budget-yfull-modified #(get-in % [:budget :file :modified]))
(rf/reg-sub :raw-data              #(get-in % [:budget :raw-data]))
(rf/reg-sub :active-data           #(get-in % [:budget :active-data]))

(rf/reg-sub :progress-bars/selected-category-ids
  #(get-in % [:progress-bars :selected-category-ids]))

(rf/reg-sub :transactions
  :<- [:active-data]
  b/transactions)

(rf/reg-sub :categories
  :<- [:active-data]
  b/categories)

(rf/reg-sub :sub-categories
  :<- [:active-data]
  b/sub-categories)

(rf/reg-sub :budget-this-month
  :<- [:active-data]
  (fn [data _]
    (b/monthly-budget data (d/this-month))))
