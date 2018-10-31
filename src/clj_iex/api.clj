(ns clj-iex.api
  (:require [aleph.http :as http]
            [byte-streams :refer [to-string]]
            [cheshire.core :refer [parse-string]]
            [clj-iex.utils :refer [s-list]]
            [clojure-csv.core :refer [parse-csv]]
            [clojure.string :refer [trim]]))

;; Data provided for free by IEX
;; See https://iextrading.com/api-exhibit-a/ for additional information
;; and conditions of use

(defonce base-url "https://api.iextrading.com/1.0/")

;; TODO handle exceptions
(defn api-call
  ([endpoint] (api-call endpoint {}))
  ([endpoint params]
   (let [parse-fn (cond (= "csv" (name (:format params :none))) parse-csv
                        (= "psv" (name (:format params :none))) #(parse-csv % :delimiter \|)
                        :else #(parse-string % true))
         query-params (reduce
                       (fn [acc [k v]]
                         (cond (nil? v) (dissoc acc k)
                               (coll? v) (if (empty? v)
                                           (dissoc acc k)
                                           (update acc k (partial s-list ",")))
                               (keyword? v) (update acc k name)
                               (string? v) (if (empty? v)
                                             (dissoc acc k)
                                             (update acc k trim))
                               :else acc))
                       params
                       params)]
     (println endpoint)
     (println query-params)
     (-> @(http/get (str base-url (s-list "/" endpoint)) {:query-params query-params})
         :body
         to-string
         parse-fn))))

