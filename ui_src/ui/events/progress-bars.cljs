(ns ui.events.progress-bars
  (:require [re-frame.core :as rf]
            [ui.helpers.reframe :refer [register-setter]]))

(register-setter [:progress-bars :selected-category-ids]
  :progress-bars/set-selected-category-ids)
