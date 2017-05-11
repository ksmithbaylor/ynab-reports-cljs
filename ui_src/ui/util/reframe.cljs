(ns ui.util.reframe
  (:require [re-frame.core :as rf :refer [after debug]]
            [re-frame.std-interceptors :refer [db-handler->interceptor]]
            [ui.db :refer [initial-state]]
            [ui.fs.persistence :refer [write-to-disk]]
            [cljs.spec :as s]))

(def draw-line
  (db-handler->interceptor
    (fn [db _]
      (.log js/console
        (apply str "%c" (repeat 80 " "))
        "border-bottom: 5px solid green")
      db)))

(defn check-and-throw
  "throw an exception if db doesn't match the spec"
  [a-spec db]
  (when-not (s/valid? a-spec db)
    (throw (ex-info (str "Invalid db: " (s/explain-str a-spec db)) {}))))

(def check-db-spec (after (partial check-and-throw :ui.db/db)))
(def persist!
  (after
    (fn [db]
      (write-to-disk db
        #(when % (.error js/console %))))))

(def standard-middleware
  [draw-line
   check-db-spec
   persist!
   debug])

(defn register-setter
  [db-key-path event]
  (rf/reg-event-db event
    standard-middleware
    (fn [db [_ arg]]
      (assoc-in db db-key-path arg))))

(defn register-reset
  [db-key-path event]
  (rf/reg-event-db event
    standard-middleware
    (fn [db _]
      (assoc-in db
        db-key-path
        (get-in initial-state db-key-path)))))
