(ns ui.subs
  (:require [re-frame.core :as rf]
            [ui.db :refer [initial-state]]))

; Expose all top-level db keys as subscriptions
(doseq [key (keys initial-state)]
  (rf/reg-sub key key))
