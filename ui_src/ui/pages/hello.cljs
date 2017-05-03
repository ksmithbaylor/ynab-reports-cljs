(ns ui.pages.hello
  (:require [re-frame.core :as rf]
            [ui.components.buttons :refer [excited-button]]))

(defn hello []
  [:div
    [excited-button]
    [:p @(rf/subscribe [:text])]])
