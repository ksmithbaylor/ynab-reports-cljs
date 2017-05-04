(ns ui.events
  (:require [re-frame.core :as rf :refer [debug]]
            [re-frame.std-interceptors :refer [db-handler->interceptor]]
            [ui.db :as db]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Helpers

; Adds middleware to every event handler
(defn inject-middleware [middlewares & args]
  (let [[event handler] (split-at 1 args)]
    (apply rf/reg-event-db
      (vec (concat event
                   [middlewares]
                   handler)))))
(def draw-line
  (db-handler->interceptor
    (fn [db _]
      (.log js/console
        "%c                       "
        "border-bottom: 5px solid green")
      db)))

; Defines what middleware to inject
(def register-event-handler
  (partial inject-middleware
    [draw-line debug]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Event handlers

(register-event-handler :initialize
  (fn [_ _]
    db/initial-state))

(register-event-handler :append
  (fn [db [_ new-str]]
    (assoc db :text
      (str (:text db)
           new-str))))

(register-event-handler :navigate
  (fn [db [_ new-route]]
    (assoc db :page new-route)))

(register-event-handler :prefs/set-budget-location
  (fn [db [_ new-location]]
    (assoc db :budget-location new-location)))
