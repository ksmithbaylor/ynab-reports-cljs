(ns ui.helpers.money)

(def BigNumber (js/require "bignumber.js"))

(defn add [a b]
  (.toNumber
    (.plus
      (BigNumber. a)
      (BigNumber. b))))
