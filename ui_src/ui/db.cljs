(ns ui.db)

(defonce initial-state
  {:budget {:file {:location      nil
                   :yfull         nil
                   :modified nil}}
   :page :preferences})
