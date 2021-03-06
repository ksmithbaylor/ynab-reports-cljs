(ns ui.pages.preferences
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [cljsjs.antd])
  (:require-macros [ui.helpers.antd :refer [antd->reagent]]))

(def ^:private electron (js/require "electron"))
(def ^:private dialog (aget electron "remote" "dialog"))

(defn- choose-location []
  (.showOpenDialog dialog
    (fn [[location]]
      (when location
        (rf/dispatch [:set-budget-location location])))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Components

(antd->reagent Button
               Card)

(defn- location-display [location modified]
  [:div
    [:p (or location "No budget location selected")]
    (when modified
      [:p (str "Last modified: " modified)])])

(defn- change-location-button [location]
  [Button {:type "secondary"
           :onClick choose-location
           :style {:margin-top "-0.5em"}}
    (if location
      "Open a different budget"
      "Select a budget")])

(defn preferences []
  (let [location @(rf/subscribe [:budget-location])
        modified @(rf/subscribe [:budget-yfull-modified])
        extra-button (r/as-element [change-location-button location])]
    [Card {:title "Budget Location"
           :extra extra-button
           :style {:top "0.5em" :right "0.5em" :bottom "initial" :left "0.5em"}}
      [location-display location modified]]))
