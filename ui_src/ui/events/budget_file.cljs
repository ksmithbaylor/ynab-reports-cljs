(ns ui.events.budget-file
  (:require [re-frame.core :as rf]
            [ui.util.reframe :refer [standard-middleware
                                     register-setter]]
            [ui.fs.budget :refer [find-latest-yfull]]
            [ui.db :as db]))

(register-setter [:budget :file :yfull]    :set-budget-yfull)
(register-setter [:budget :file :modified] :set-budget-yfull-modified)

(rf/reg-event-fx :set-budget-location
  standard-middleware
  (fn [world [_ location]]
    (if location
      {:db (assoc-in (:db world) [:budget :file :location] location)
       :find-latest-yfull location}
      {:db (assoc-in (:db world) [:budget :file]
             (get-in db/initial-state [:budget :file]))})))

(rf/reg-event-fx :set-budget-location-failure
  standard-middleware
  (fn [_ _]
    {:alert "Invalid budget location"
     :dispatch [:set-budget-location nil]}))

(rf/reg-fx :find-latest-yfull
  (fn [location]
    (find-latest-yfull location
      (fn [err {:keys [file modified]}]
        (if (some? err)
          (rf/dispatch [:set-budget-location-failure])
          (do
            (rf/dispatch [:set-budget-yfull file])
            (rf/dispatch [:set-budget-yfull-modified modified])))))))
