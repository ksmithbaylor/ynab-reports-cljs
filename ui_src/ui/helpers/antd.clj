(ns ui.helpers.antd
  (:require [reagent.core :as r]))

(defn- has-dots? [sym]
  (not (clojure.string/blank? (re-matches #".*\..*" (str sym)))))

(defn- to-property-access [name]
  (symbol (str ".-" name)))

(defmacro adapt-single [component]
  `(def ^:private ~component
    (r/adapt-react-class
      (~(to-property-access `~component)
        js/antd))))

(defmacro adapt-nested [component]
  (let [heirarchy (-> component
                      str
                      (clojure.string/split #"\."))
              access-symbols (map to-property-access heirarchy)
              final-name (symbol (clojure.string/join "-" heirarchy))]
    `(def ^:private ~final-name
       (r/adapt-react-class
          (-> js/antd ~@access-symbols)))))

(defmacro antd->reagent [& components]
  `(do
    ~@(for [component components]
        (if (has-dots? component)
          (macroexpand `(adapt-nested ~component))
          (macroexpand `(adapt-single ~component))))))
