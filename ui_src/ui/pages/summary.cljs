(ns ui.pages.summary
  (:require [re-frame.core :as rf]))

(defn summary []
  (let [transactions @(rf/subscribe [:transactions])]
    [:div
      [:p
        (if transactions
          (str (count transactions) " total transactions.")
          "No transactions.")]]))
