(ns ui.subs
  (:require [re-frame.core :as rf])
  (:require-macros [ui.helpers.reframe :refer [expose-keys]]))

(expose-keys :text)
