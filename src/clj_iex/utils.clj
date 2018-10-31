(ns clj-iex.utils
  (:require [clojure.string :refer [join trim]]))

(defn s-list [d xs]
  (join d (map #(trim (name %)) xs)))

