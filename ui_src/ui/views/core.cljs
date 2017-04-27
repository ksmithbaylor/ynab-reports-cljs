(ns ui.views.core
  (:require [ui.views.header :refer [header]]
            [ui.views.sidebar :refer [sidebar]]
            [ui.views.main :refer [main]]))

(defn app []
  [:div {:class "ui"}
    [header]
    [sidebar]
    [main]])
