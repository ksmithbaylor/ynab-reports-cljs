(ns ui.db
  (:require [cljs.spec :as s]
            [clojure.string :refer [starts-with? ends-with?]])
  (:require-macros [ui.helpers.spec :refer [strict-keys]]))

(defonce initial-state
  {:budget              {:raw-data               nil
                         :active-data            nil
                         :file                   {:location             nil
                                                  :yfull                nil
                                                  :modified             nil}}
   :page                 :category-projections
   :loading              {:background            true
                          :total                 true
                          :message               ""}
   :category-projections {:selected-category-ids #{}}})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Strict specs for the db shape

(s/def ::location              (s/nilable
                                (s/and string?
                                       #(starts-with? % "/"))))
(s/def ::yfull                 (s/nilable
                                (s/and string?
                                       #(ends-with? % ".yfull"))))
(s/def ::modified              (s/nilable #(instance? js/Date %)))
(s/def ::file                  (strict-keys ::location ::yfull ::modified))
(s/def ::raw-data              (s/nilable map?))
(s/def ::active-data           (s/nilable map?))
(s/def ::budget                (strict-keys ::file ::raw-data ::active-data))
(s/def ::page                  #{:preferences :category-projections})
(s/def ::background            boolean?)
(s/def ::total                 boolean?)
(s/def ::message               string?)
(s/def ::loading               (strict-keys ::background ::total ::message))
(s/def ::selected-category-ids set?)
(s/def ::category-projections  (strict-keys ::selected-category-ids))
(s/def ::db                    (strict-keys ::budget ::page ::loading ::category-projections))
