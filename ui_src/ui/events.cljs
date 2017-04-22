(ns ui.events
  (:require [re-frame.core :as rf]
            [ui.db :as db]))

(rf/reg-event-db
  :initialize
  (fn [_ _]
    db/initial-state))

(rf/reg-event-db
  :append
  (fn [db [_ new-str]]
    (assoc db :text
      (str (:text db)
           new-str))))
