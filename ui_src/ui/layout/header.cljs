(ns ui.layout.header)

(defn header []
  [:header {:class "ui__header"}
    [:h1
      [:span.ynab "YNAB"]
      " "
      [:span.helper "Helper"]]])
