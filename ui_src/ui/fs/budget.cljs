(ns ui.fs.budget)

(def fs (js/require "fs"))
(def glob ((js/require "glob-fs")))

(defn all-budget-files
  [budget-dir]
  (let [search (str budget-dir "/**/*.yfull")
        files (.readdirSync glob search #js {:cwd "/"})]
    (map (partial str "/") files)))

(defn most-recent-file
  [files]
  (let [stats (map #(.statSync fs %1) files)
        times (map #(.-mtime %1) stats)
        files-with-times (map (fn [file time]
                                {:file file :time time})
                              files
                              times)]
    (apply max-key :time files-with-times)))

(defn latest-budget-file
  [budget-dir]
  (-> budget-dir
      all-budget-files
      most-recent-file))
