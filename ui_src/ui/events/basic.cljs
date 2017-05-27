(ns ui.events.basic
  (:require [re-frame.core :as rf]
            [ui.helpers.reframe :refer [register-setter]])
  (:require-macros [com.rpl.specter :refer [setval]]))

(register-setter [:page] :navigate)

(rf/reg-fx :alert
  (fn [message]
    (js/alert message)))

(rf/reg-event-db :set-loading
  (fn [db [_ & args]]
    (let [path (vec (butlast args))
          value (last args)]
      (setval (vec (cons :loading path)) value db))))

(register-setter [:loading :message] :set-loading-message)
