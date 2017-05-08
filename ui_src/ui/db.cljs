(ns ui.db
  (:require [cljs.spec :as s])
  (:require-macros [ui.util.spec :refer [strict-keys]]))

(defonce initial-state
  {:budget {:file {:location nil
                   :yfull    nil
                   :modified nil}
            :raw-data nil}
   :page :preferences})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Strict specs for the db shape

(s/def ::location (s/nilable
                    (s/and string?
                           #(clojure.string/starts-with? % "/"))))
(s/def ::yfull    (s/nilable
                    (s/and string?
                           #(clojure.string/ends-with? % ".yfull"))))
(s/def ::modified (s/nilable #(instance? js/Date %)))
(s/def ::file     (strict-keys ::location ::yfull ::modified))
(s/def ::raw-data (s/nilable map?))
(s/def ::budget   (strict-keys ::file ::raw-data))
(s/def ::page     #{:preferences})
(s/def ::db       (strict-keys ::budget ::page))
