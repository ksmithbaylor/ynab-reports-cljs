(ns ui.layout.header)

(defn header []
  [:header.ui__header
    [:h1
      [:span.ynab "YNAB"]
      " "
      [:span.helper "Helper"]]])
