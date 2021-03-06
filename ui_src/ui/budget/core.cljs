(ns ui.budget.core
  (:require-macros [com.rpl.specter :refer [select select-one setval transform]])
  (:require [com.rpl.specter :as sp]
            [clojure.string :refer [starts-with?]]))

(defn filter-by [k pred]
  (if (ifn? pred)
    (sp/filterer #(-> % k pred))
    (sp/filterer #(= (k %) pred))))

(defn active-data [data]
  (when data
    (let [all-maps (sp/recursive-path [] p
                     [(sp/walker vector?)
                      sp/ALL
                      (sp/continue-then-stay p)])]
      (setval [all-maps (sp/pred :isTombstone)]
              sp/NONE
              data))))

(defn transactions [data _]
  (when data
    (:transactions data)))

(defn categories [data _]
  (when data
    (sort-by :sortableIndex (:masterCategories data))))

(defn sub-categories [data _]
  (when data
    (sort-by :sortableIndex
      (select [:masterCategories
               sp/ALL
               :subCategories
               sp/ALL]
              data))))

(defn monthly-budget [data date]
  (when data
    (select-one [:monthlyBudgets
                 (filter-by :month #(starts-with? % date))
                 (sp/filterer #(-> % :month (starts-with? date)))
                 sp/FIRST]
      data)))

(defn transactions-for-month [transactions month]
  (when transactions
    (->> transactions
      (filter #(starts-with? (:date %) month))
      (map #(condp = (:subTransactions %)
              nil %
              [] %
              (cons % (:subTransactions %))))
      (flatten))))
