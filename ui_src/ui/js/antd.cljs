(ns ui.js.antd
  (:require [reagent.core :as r]
            [cljsjs.antd]
            [clojure.string :refer [capitalize join]]
            [cljs.compiler :as compiler]
            [cljs.js :refer [empty-state js-eval eval]]))

(enable-console-print!)

(defn isCapitalized [str]
  (= str (capitalize str)))

(def q (atom #queue []))

(defn add-capital-entries
  [obj path]
  (doseq [entry (js->clj (.entries js/Object obj))]
    (when (isCapitalized (first entry))
      (println "-- Adding" (first entry) path)
      (swap! q conj [entry path]))))

(add-capital-entries js/antd [])

(defn process-item
  [[[name component] path]]
  (let [new-path (conj path name)
        clj-name (join "-" new-path)]
    (println "Defining" clj-name)
    (eval (empty-state)
         `(def ~(symbol clj-name) 42)
          {:eval js-eval :def-emits-var true :verbose true :ns 'ui.js.antd :context :expr})
    (add-capital-entries component new-path)))

(loop [entry (peek @q)]
  (when-not (nil? entry)
    (swap! q pop)
    (process-item entry)
    (recur (peek @q))))
