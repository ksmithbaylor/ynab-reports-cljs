(ns ui.util.reframe
  (:require [re-frame.core :as rf :refer [debug]]
            [re-frame.std-interceptors :refer [db-handler->interceptor]]))

(def draw-line
  (db-handler->interceptor
    (fn [db _]
      (.log js/console
        (apply str "%c" (repeat 80 " "))
        "border-bottom: 5px solid green")
      db)))

(def standard-middleware
  [draw-line
   debug])

(defn register-setter
  ([db-key-path event]
   (rf/reg-event-db event
     standard-middleware
     (fn [db [_ arg]]
       (assoc-in db db-key-path arg)))))
