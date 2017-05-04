(ns ui.pages.preferences
  (:require [re-frame.core :as rf]
            [reagent.core :as r])
  (:require-macros [ui.helpers.antd :refer [antd->reagent]]))

(def ^:private electron (js/require "electron"))
(def ^:private dialog (aget electron "remote" "dialog"))

(antd->reagent Button
               Card)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Helpers

(defn- handleFilePicked [[file]]
  (rf/dispatch [:prefs/set-budget-location file]))

(defn- chooseFile []
  (.showOpenDialog dialog handleFilePicked))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Components

(defn- location-display [location]
  [:p
    (if (nil? location)
      "No budget location selected"
      location)])

(defn- change-file-button [location]
  [Button {:type "secondary"
           :onClick chooseFile
           :style {:margin-top "-0.5em"}}
    (if (nil? location)
      "Select a budget"
      "Open a different budget")])

(defn preferences []
  (let [location @(rf/subscribe [:budget-location])
        extra-button (r/as-element [change-file-button location])]
    [Card {:title "Budget Location"
           :extra extra-button}
      [location-display location]]))
