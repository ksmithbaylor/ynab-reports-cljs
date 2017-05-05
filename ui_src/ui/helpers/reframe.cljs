(ns ui.helpers.reframe
  (:require [re-frame.std-interceptors :refer [db-handler->interceptor]]))

(def draw-line
  (db-handler->interceptor
    (fn [db _]
      (.log js/console
        (apply str "%c" (repeat 80 " "))
        "border-bottom: 5px solid green")
      db)))
