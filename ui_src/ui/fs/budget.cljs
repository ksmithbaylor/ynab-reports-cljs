(ns ui.fs.budget)

(def ^:private fs (js/require "fs"))
(def ^:private glob ((js/require "glob-fs")))

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
    (.readdir glob search #js {:cwd "/"}
      (fn [err files]
        (if (some? err)
          (cb err nil)
          (let [absolute-paths (map (partial str "/") files)
                latest (most-recent-file absolute-paths)]
            (cb nil latest)))))))
