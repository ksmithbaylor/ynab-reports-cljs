(ns ui.events.boot
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [ui.db :as db]
            [ui.layout.core :refer [app]]
            [ui.util.reframe :refer [standard-middleware
                                     register-setter]]))

(rf/reg-event-fx :initialize
  standard-middleware
  (fn [_ _]
    {:db db/initial-state
     :start-app nil}))

(rf/reg-event-fx :deeplink
  standard-middleware
  (fn [world [_ state]]
    {:db db/initial-state
     :deeplink state}))

(rf/reg-fx :deeplink
  (fn [state]
    (when-let [location (get-in state [:budget :file :location])]
      (rf/dispatch [:set-budget-location location]))
    (rf/dispatch [:start-app])))

(rf/reg-event-fx :start-app
  standard-middleware
  (fn [_ _]
    {:start-app nil}))

(defn clear-preloader! []
  (let [preloader (.getElementById js/document "preloader")]
    (.add (.-classList preloader) "fulfilled")))

(defn attach-fullscreen-handlers! []
  (let [win (.getCurrentWindow (.-remote (js/require "electron")))]
    (.on win "enter-full-screen"
      #(.add (aget js/document "body" "classList") "fullscreen"))
    (.on win "leave-full-screen"
      #(.remove (aget js/document "body" "classList") "fullscreen"))))

(rf/reg-fx :start-app
  (fn [_]
    (attach-fullscreen-handlers!)
    (rf/clear-subscription-cache!)
    (r/render
      [app]
      (.getElementById js/document "app-container"))
    (clear-preloader!)))
