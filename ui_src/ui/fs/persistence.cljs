(ns ui.fs.persistence
  (:require [ui.db :refer [initial-state]]
            [cljs.reader :refer [read-string]]))

(def fs (js/require "fs"))
(def mkdirp (js/require "mkdirp"))

(def data-directory (.getPath (aget (js/require "electron")
                                    "remote"
                                    "app")
                              "userData"))
(def db-file (str data-directory "/data.edn"))

; https://crossclj.info/ns/clodash/0.42.0/clodash.map.html#_select-paths
(defn select-paths
  "Similar to select-keys, just the 'key' here is a path in the nested map"
  [m & paths]
  (reduce
   (fn [result path]
     (assoc-in result path (get-in m path)))
   {}
   paths))

; https://crossclj.info/ns/clodash/0.42.0/clodash.map.html#_rmerge
(defn recursive-merge
  "Recursive merge of the provided maps."
  [& maps]
  (if (every? (some-fn map? nil?) maps)
    (apply merge-with recursive-merge maps)
    (last maps)))

(defn serialize [db]
  (select-paths db
    [:budget :file :location]
    [:category-projections]))

(defn deserialize [db]
  (recursive-merge initial-state db))

(def last-written (atom nil))

(defn write-to-disk [db cb]
  (mkdirp data-directory
    (fn [err]
      (if err
        (cb err)
        (let [payload (serialize db)]
          (if (= payload @last-written)
            (cb nil)
            (let [payload-str (pr-str payload)]
              (.writeFile fs db-file payload-str
                (fn [err]
                  (reset! last-written payload)
                  (cb (or err nil)))))))))))

(defn read-from-disk [cb]
  (.readFile fs db-file
    (fn [err buffer]
      (if (some? err)
        (cb err nil)
        (cb nil (->> (.toString buffer)
                     read-string
                     deserialize))))))
