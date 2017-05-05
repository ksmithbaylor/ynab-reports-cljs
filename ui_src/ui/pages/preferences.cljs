(ns ui.pages.preferences
  (:require [re-frame.core :as rf]
            [reagent.core :as r])
  (:require-macros [ui.util.antd :refer [antd->reagent]]))

(def ^:private electron (js/require "electron"))
(def ^:private dialog (aget electron "remote" "dialog"))

(defn- choose-location []
  (.showOpenDialog dialog
    #(rf/dispatch [:set-budget-location (first %1)])))

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
           :extra extra-button}
      [location-display location modified]]))
