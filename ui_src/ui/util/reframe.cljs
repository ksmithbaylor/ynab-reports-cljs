(ns ui.util.reframe
  (:require [re-frame.core :as rf :refer [after debug]]
            [re-frame.std-interceptors :refer [db-handler->interceptor]]
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
  (println (str db) "conforms:" (s/valid? a-spec db))
  (println (s/conform a-spec db))
  (when-not (s/valid? a-spec db)
    (throw (ex-info (str "spec check failed: " (s/explain-str a-spec db)) {}))))

(def check-spec (after (partial check-and-throw :ui.db/db)))

(def standard-middleware
  [draw-line
   check-spec
   debug])

(defn register-setter
  ([db-key-path event]
   (rf/reg-event-db event
     standard-middleware
     (fn [db [_ arg]]
       (assoc-in db db-key-path arg)))))
