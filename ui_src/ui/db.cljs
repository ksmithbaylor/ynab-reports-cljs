(ns ui.db)

(defonce initial-state
  {:text "Hola mundo"
   :budget {:file {:location      nil
                   :yfull         nil
                   :modified nil}}
   :page :hello})
