(ns ui.layout.core
  (:require [ui.layout.header :refer [header]]
            [ui.layout.sidebar :refer [sidebar]]
            [ui.layout.main :refer [main]]))

(defn app []
  [:div {:class "ui"}
    [header]
    [sidebar]
    [main]])
