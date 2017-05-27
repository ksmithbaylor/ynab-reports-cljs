(ns ui.db
  (:require [cljs.spec :as s]
            [clojure.string :refer [starts-with? ends-with?]])
  (:require-macros [ui.helpers.spec :refer [strict-keys]]))

(defonce initial-state
  {:budget        {:raw-data              nil
                   :active-data           nil
                   :file                  {:location      nil
                                           :yfull         nil
                                           :modified      nil}}
   :page          :summary
   :loading       {:background            true
                   :total                 true
                   :single-page           {:summary       false
                                           :preferences   false
                                           :progress-bars false}
                   :message               "testing"}
   :progress-bars {:selected-category-ids #{}}})

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
(s/def ::page                  #{:summary :preferences :progress-bars})
(s/def ::background            boolean?)
(s/def ::total                 boolean?)
(s/def ::single-page           map?)
(s/def ::message               string?)
(s/def ::loading               (strict-keys ::background ::total ::single-page ::message))
(s/def ::selected-category-ids set?)
(s/def ::progress-bars         (strict-keys ::selected-category-ids))
(s/def ::db                    (strict-keys ::budget ::page ::loading ::progress-bars))
