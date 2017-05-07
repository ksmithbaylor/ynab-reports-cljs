(ns ui.events.budget-file
  (:require [re-frame.core :as rf]
            [ui.util.reframe :refer [standard-middleware
                                     register-setter
                                     register-reset]]
            [ui.fs.budget :refer [find-latest-yfull]]
            [ui.db :as db]))

(register-setter [:budget :file :location] :set-budget-location-success)
(register-setter [:budget :file :yfull]    :set-budget-yfull)
(register-setter [:budget :file :modified] :set-budget-yfull-modified)
(register-reset  [:budget :file]           :reset-budget-file)

(rf/reg-event-fx :set-budget-location
  standard-middleware
  (fn [world [_ location valid]]
    {:find-latest-yfull location}))

(rf/reg-event-fx :set-budget-location-failure
  standard-middleware
  (fn [_ _]
    {:alert "Invalid budget location"
     :dispatch [:reset-budget-file]}))

(rf/reg-fx :find-latest-yfull
  (fn [location]
    (find-latest-yfull location
      (fn [err {:keys [file modified]}]
        (if (or (some? err) (nil? file))
          (rf/dispatch [:set-budget-location-failure])
          (do
            (rf/dispatch [:set-budget-location-success location])
            (rf/dispatch [:set-budget-yfull file])
            (rf/dispatch [:set-budget-yfull-modified modified])))))))
