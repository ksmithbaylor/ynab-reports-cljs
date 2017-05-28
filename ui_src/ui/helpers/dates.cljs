(ns ui.helpers.dates
  (:require [goog.string :as gs]
            [goog.string.format]))

(defn this-month []
  (let [today (js/Date.)
        m (+ 1 (.getMonth today))
        y (.getFullYear today)]
    (gs/format "%d-%02d" y m)))
