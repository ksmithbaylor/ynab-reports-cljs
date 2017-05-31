(ns ui.events.basic
  (:require [re-frame.core :as rf]
            [ui.helpers.reframe :refer [register-setter]])
  (:require-macros [com.rpl.specter :refer [setval]]))

(register-setter [:page] :navigate)

(rf/reg-fx :alert
  (fn [message]
    (js/alert message)))

(register-setter [:loading :total] :set-loading-total)
(register-setter [:loading :background] :set-loading-background)
(register-setter [:loading :message] :set-loading-message)
