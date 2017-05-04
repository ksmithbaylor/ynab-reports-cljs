(ns ui.events
  (:require [re-frame.core :as rf :refer [debug]]
            [ui.db :as db]))

; Helper to add middleware to every event handler
(defn inject-middleware [middlewares & args]
  (let [[event handler] (split-at 1 args)]
    (apply rf/reg-event-db
      (vec (concat event
                   [middlewares]
                   handler)))))

; Define what middleware to inject
(def register-event-handler
  (partial inject-middleware
    [debug]))

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
