(ns clj-iex.stats
  (:require [clj-iex.api :refer [api-call]]))

(def ep (partial conj ["stats"]))

(defn get-intraday
  "Returns intraday stats."
  [& {:as params}]
  (api-call (ep "intraday") params))

(defn get-recent
  "Returns recent stats."
  [& {:as params}]
  (api-call (ep "recent") params))

(defn get-records
  "Returns record stats."
  [& {:as params}]
  (api-call (ep "records") params))

(defn get-historical-summary
  "Returns historical stats.
  All parameters are optional and should be specified as key value pairs.
  `:date` specified in YYYYMM format.
  `:format` can only be csv, when omitted defaults to json."
  [& {:as params}]
  (api-call (ep "historical") params))

(defn get-historical-daily
  "Returns daily stats for a given month or day.
  All parameters are optional and should be specified as key value pairs.
  `:date` specified in YYYYMM or YYYYMMDD format.
  `:last` used in place of date, indicating the number of trading days to go back.
  `:format` can only be csv, when omitted defaults to json.
  See https://iextrading.com/developer/docs/#historical-daily for further info."
  [& {:as params}]
  (api-call (ep "historical" "daily") params))

