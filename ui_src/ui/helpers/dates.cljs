(ns ui.helpers.dates
  (:require [goog.string :as gs]
            [goog.string.format]))

(defn this-month []
  (let [today (js/Date.)
        m (+ 1 (.getMonth today))
        y (.getFullYear today)]
    (gs/format "%d-%02d" y m)))

(defn current-day
  ([]
   (current-day (js/Date.)))
  ([date]
   (.getDate date)))


(defn days-in-month
  ([]
   (days-in-month (js/Date.)))
  ([date]
   (.getDate
     (js/Date. (.getFullYear date)
               (+ 1 (.getMonth date))
               0))))
