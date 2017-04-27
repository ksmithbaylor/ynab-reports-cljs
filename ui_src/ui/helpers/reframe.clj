(ns ui.helpers.reframe)

; Create subscriptions for several keys in the db,
; using the key itself as the selector
(defmacro expose-keys [& keys]
  `(do
    ~@(for [key keys]
        `(rf/reg-sub ~key ~key))))
