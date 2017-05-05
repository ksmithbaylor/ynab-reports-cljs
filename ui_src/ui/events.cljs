(ns ui.events
  (:require [re-frame.core :as rf :refer [debug]]
            [ui.helpers.reframe :refer [draw-line]]
            [ui.fs.budget :refer [find-latest-yfull]]
            [ui.db :as db]))

(def standard-middleware
  [draw-line
   debug])

(defn register-setter
  ([db-key-path event]
   (rf/reg-event-db event
     standard-middleware
     (fn [db [_ arg]]
       (assoc-in db db-key-path arg)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Event handlers

(rf/reg-event-db :initialize
  standard-middleware
  (fn [_ _]
    db/initial-state))

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

(register-setter [:page]                   :navigate)
(register-setter [:budget :file :yfull]    :set-budget-yfull)
(register-setter [:budget :file :modified] :set-budget-yfull-modified)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Effects

(rf/reg-fx :find-latest-yfull
  (fn [location]
    (find-latest-yfull location
      (fn [err {:keys [file modified]}]
        (if (some? err)
          (rf/dispatch [:set-budget-location-failure])
          (do
            (rf/dispatch [:set-budget-yfull file])
            (rf/dispatch [:set-budget-yfull-modified modified])))))))

(rf/reg-fx :alert
  (fn [message]
    (js/alert message)))
