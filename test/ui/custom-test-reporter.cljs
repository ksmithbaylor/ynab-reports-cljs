(ns ui.custom-test-reporter
  (:require [cljs.test :as t]
            [clojure.string :refer [join]]))

; State of test suite
(def dots (atom []))
(def mishaps (atom []))

; Color escape sequences
(def g "\u001B[32m")
(def r "\u001B[31m")
(def y "\u001B[33m")
(def esc "\u001B[m")
(defn green [s]  (str g s esc))
(defn red [s]    (str r s esc))
(defn yellow [s] (str y s esc))

(defn mishap [m type]
  {:type         type
   :m            m
   :vars-str     (t/testing-vars-str m)
   :contexts     (:testing-contexts (t/get-current-env))
   :contexts-str (t/testing-contexts-str)})

(defmethod t/report [:cljs.test/default :pass] [m]
  (swap! dots conj (green "."))
  (t/inc-report-counter! :pass))

(defmethod t/report [:cljs.test/default :fail] [m]
  (t/inc-report-counter! :fail)
  (swap! dots conj (red "F"))
  (swap! mishaps conj (mishap m :fail)))

(defmethod t/report [:cljs.test/default :error] [m]
  (t/inc-report-counter! :error)
  (swap! dots conj (yellow "E"))
  (swap! mishaps conj (mishap m :error)))

(defn print-mishap [m]
  (let [[color title] (case (:type m)
                        :error [y "ERROR"]
                        :fail  [r "FAIL"])]
    (print color)
    (println title "in" (:vars-str m)))
  (when (seq (:contexts m))
    (println (:contexts-str m)))
  (when-let [message (:message (:m m))] (println message))
  (t/print-comparison (:m m))
  (print esc))

(defmethod t/report [:cljs.test/default :summary] [m]
  (print "\u001B[2J\u001B[H\u001B[f")
  (println (join "\n" (map (partial apply str)
                           (partition-all 50 @dots)))
           "\n")
  (doseq [m @mishaps] (print-mishap m))
  (println (str "\n" (:test m)) "tests,"
    (+ (:pass m) (:fail m) (:error m)) "assertions")
  (let [success (and (= 0 (:fail m))
                     (= 0 (:error m)))
        errors (< 0 (:error m))
        failures (< 0 (:fail m))]
    (println " " (if success g esc)                 (:pass m)  "passed")
    (println " " (if success g (if failures r esc)) (:fail m)  "failed")
    (println " " (if success g (if errors y esc))   (:error m) "errored"))
  (print esc))

(defmethod t/report [:cljs.test/default :begin-test-ns] [m])
