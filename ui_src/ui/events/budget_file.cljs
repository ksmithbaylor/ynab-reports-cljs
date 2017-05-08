(ns ui.events.budget-file
  (:require [re-frame.core :as rf]
            [ui.util.reframe :refer [standard-middleware
                                     register-setter
                                     register-reset]]
            [ui.fs.budget :refer [find-latest-yfull
                                  read-yfull-data]]
            [ui.db :as db]))

(rf/reg-event-fx :set-budget-location
  standard-middleware
  (fn [world [_ location valid]]
    {:find-latest-yfull location}))

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

(register-setter [:budget :file :location] :set-budget-location-success)

(rf/reg-event-fx :set-budget-location-failure
  standard-middleware
  (fn [_ _]
    {:alert "Invalid budget location"
     :dispatch [:reset-budget-file]}))

(rf/reg-event-fx :set-budget-yfull
  standard-middleware
  (fn [world [_ path]]
    {:db (assoc-in (:db world) [:budget :file :yfull] path)
     :read-yfull-data path}))

(rf/reg-fx :read-yfull-data
  (fn [path]
    (read-yfull-data path
      (fn [err clj-data]
        (if (or (some? err) (nil? clj-data))
          (rf/dispatch [:read-yfull-data-failure])
          (rf/dispatch [:set-budget-raw-data clj-data]))))))

(register-setter [:budget :raw-data] :set-budget-raw-data)

(rf/reg-event-fx :read-yfull-data-failure
  standard-middleware
  (fn [_ _]
    {:alert "Budget file may be corrupted"
     :dispatch-n [[:reset-budget-file]
                  [:reset-budget-raw-data]]}))

(register-reset  [:budget :raw-data] :reset-budget-raw-data)

(register-setter [:budget :file :modified] :set-budget-yfull-modified)
(register-reset  [:budget :file] :reset-budget-file)
