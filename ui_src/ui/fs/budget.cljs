(ns ui.fs.budget
  (:require [ui.budget.core :as b]))

(def ^:private fs (js/require "fs"))
(def ^:private glob (js/require "glob"))

(defn- async-parse-json [buffer]
  (-> (.resolve js/Promise buffer)
      (.then #(js/Response. %))
      (.then #(.json %))))

(defn- most-recent-file
  [files]
  (let [stats (map #(.statSync fs %1) files)
        times (map #(.-mtime %1) stats)
        files-with-times (map (fn [file modified]
                                {:file file :modified modified})
                              files
                              times)]
    (apply max-key :modified files-with-times)))

(defn find-latest-yfull
  [location cb]
  (let [search (str location "/**/*.yfull")]
    (glob search
      (fn [err files]
        (if (some? err)
          (cb err nil)
          (cb nil (most-recent-file files)))))))

(defn read-yfull-data
  [path cb]
  (.readFile fs path
    (fn [err buffer]
      (if (or (some? err) (nil? buffer))
        (cb err nil)
        (-> (async-parse-json buffer)
            (.then #(js->clj % :keywordize-keys true))
            (.then #(identity [% (b/active-data %)]))
            (.then #(cb nil %)))))))
