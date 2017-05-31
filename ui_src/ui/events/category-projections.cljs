(ns ui.events.category-projections
  (:require [re-frame.core :as rf]
            [ui.helpers.reframe :refer [register-setter]]))

(register-setter [:category-projections :selected-category-ids]
  :category-projections/set-selected-category-ids)
