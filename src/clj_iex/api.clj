(ns clj-iex.api
  (:require [aleph.http :as http]
            [byte-streams :refer [to-string]]
            [cheshire.core :refer [parse-string]]
            [clojure-csv.core :refer [parse-csv]]
            [clojure.string :refer [join trim]]))

;; Data provided for free by IEX
;; See https://iextrading.com/api-exhibit-a/ for additional information
;; and conditions of use

(defonce base-url "https://api.iextrading.com/1.0/")

(defn s-list [d xs]
  (join d (map #(trim (name %)) xs)))

(defn get-url [endpoint]
  (str base-url (s-list "/" endpoint)))

(defn sanitize-params [params]
  (reduce
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
   params))

(defn get-parse-fn [format]
  (condp = format
    "csv" parse-csv
    "psv" #(parse-csv % :delimiter \|)
    #(parse-string % true)))

(defn api-call
  ([endpoint] (api-call endpoint {}))
  ([endpoint params]
   (let [url (get-url endpoint)
         query-params {:query-params (sanitize-params params)}
         parse-fn (get-parse-fn (:format query-params))]
     (try
       (-> @(http/get url query-params)
           :body
           to-string
           parse-fn)
       (catch Exception e
         (println "Error making request.")
         {:error-message (.getMessage e)})))))

