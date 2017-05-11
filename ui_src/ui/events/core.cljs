(ns ui.events.core
  (:require [re-frame.core :as rf]
            [ui.util.reframe :refer [register-setter]]
            [ui.events.boot]
            [ui.events.budget-file]))

(register-setter [:page] :navigate)

(rf/reg-fx :alert
  (fn [message]
    (js/alert message)))
