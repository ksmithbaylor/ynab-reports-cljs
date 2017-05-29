(ns ui.helpers.money)

(def BigNumber (js/require "bignumber.js"))

(defn add [a b]
  (.toNumber
    (.plus
      (BigNumber. a)
      (BigNumber. b))))

(defn dollars [amount]
  (str "$"
    (js/String.
      (.toFixed
        (js/Number. amount)
        2))))
