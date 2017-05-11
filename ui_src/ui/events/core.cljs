(ns ui.events.core
  (:require [re-frame.core :as rf]
            [ui.db :as db]
            [ui.util.reframe :refer [standard-middleware
                                     register-setter]]
            [ui.events.budget-file]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Generic event handlers

(rf/reg-event-db :initialize
  standard-middleware
  (fn [_ [_ state]]
    (or state db/initial-state)))

(register-setter [:page] :navigate)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Generic effects

(rf/reg-fx :alert
  (fn [message]
    (js/alert message)))
