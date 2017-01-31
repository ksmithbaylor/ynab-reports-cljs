(ns ui.test-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [ui.core-test]))

(doo-tests 'ui.core-test)
