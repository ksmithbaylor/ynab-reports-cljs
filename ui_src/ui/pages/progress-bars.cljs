(ns ui.pages.progress-bars
  (:require [re-frame.core :as rf]
            [clojure.pprint :refer [pprint]]))

(defn progress-bars []
  (let [budget @(rf/subscribe [:budget-this-month])]
    [:p (str "Budget: " (with-out-str (pprint budget)))]))
