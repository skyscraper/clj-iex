(ns clj-iex.markets
  (:require [clj-iex.api :refer [api-call]]))

(defn get-market
  "Returns near real time traded volume on the markets.
  `format` optional; can only be csv, when omitted defaults to json."
  [& {:as params}]
  (api-call ["market"] params))
