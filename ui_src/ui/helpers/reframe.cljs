(ns ui.helpers.reframe
  (:require [re-frame.core :as rf :refer [after]]
            [re-frame.interceptor :refer [->interceptor get-effect get-coeffect]]
            [re-frame.std-interceptors :refer [db-handler->interceptor]]
            [clojure.data :as data]
            [ui.db :refer [initial-state]]
            [ui.fs.persistence :refer [write-to-disk]]
            [cljs.spec :as s]))

(defn check-and-throw
  "throw an exception if db doesn't match the spec"
  [a-spec db]
  (when-not (s/valid? a-spec db)
    (throw (ex-info (str "Invalid db: " (s/explain-str a-spec db)) {}))))

(def check-db-spec
  (after (partial check-and-throw :ui.db/db)))

(def persist!
  (after
    (fn [db]
      (write-to-disk db
        #(when % (js/console.error %))))))

(def debug
  (->interceptor
    :id     :debug
    :before (fn debug-before
              [context]
              (let [event (get-coeffect context :event)
                    event-name (first event)]
                (js/console.groupCollapsed
                  (str "%c" event-name (apply str (repeat (- 80 (count (str event-name))) " ")))
                  "color: #3e4147; border-top: 3px solid #0082D0")
                (js/console.groupCollapsed "event")
                (js/console.log event)
                (js/console.groupEnd))
              context)
    :after  (fn debug-after
              [context]
              (let [event   (get-coeffect context :event)
                    orig-db (get-coeffect context :db)
                    new-db  (get-effect   context :db ::not-found)]
                (if (= new-db ::not-found)
                  (js/console.log "No db changes")
                  (let [[only-before only-after] (data/diff orig-db new-db)
                        db-changed?    (or (some? only-before) (some? only-after))]
                    (if db-changed?
                      (do (js/console.groupCollapsed "diff")
                          (js/console.log "only before:" only-before)
                          (js/console.log "only after :" only-after)
                          (js/console.groupEnd))
                      (js/console.log "no app-db changes caused by:" event))
                    (js/console.groupCollapsed "db")
                    (js/console.log new-db)
                    (js/console.groupEnd)))
                (js/console.groupEnd)
                context))))

(def standard-middleware
  [check-db-spec
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
