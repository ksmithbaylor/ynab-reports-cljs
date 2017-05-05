(ns ui.util.spec
  (:require [cljs.spec :as s]))

(defmacro strict-keys [& keys]
  `(s/and
     (s/keys :req-un ~(vec keys))
     #(= (count %) ~(count keys))))
