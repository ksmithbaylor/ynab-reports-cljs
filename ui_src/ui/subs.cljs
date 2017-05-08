(ns ui.subs
  (:require [re-frame.core :as rf]
            [ui.db :refer [initial-state]]))

; Expose all top-level db keys as subscriptions
(doseq [key (keys initial-state)]
  (rf/reg-sub key key))

(rf/reg-sub :budget-location       #(get-in %1 [:budget :file :location]))
(rf/reg-sub :budget-yfull-file     #(get-in %1 [:budget :file :yfull]))
(rf/reg-sub :budget-yfull-modified #(get-in %1 [:budget :file :modified]))
(rf/reg-sub :transactions          #(get-in %1 [:budget :raw-data "transactions"]))
