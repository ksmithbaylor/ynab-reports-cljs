(ns ui.subs
  (:require [re-frame.core :as rf]))

; Create subscriptions for several keys in the db,
; using the key itself as the selector
(defn- expose-keys [& keys]
  (doseq [key keys]
    (rf/reg-sub key key)))

(expose-keys :text)
