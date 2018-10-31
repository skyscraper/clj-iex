(ns clj-iex.refdata
  (:require [clj-iex.api :refer [api-call]]))

(def ep (partial conj ["ref-data"]))

(defn get-symbols
  "Returns symbols supported on IEX.
  `:format` optional; can only be csv, when omitted defaults to json."
  [& {:as params}]
  (api-call (ep "symbols") params))

(defn get-corporate-actions
  "Returns corporate actions for IEX listed securities.
  Parameters are optional and should be specified as key value pairs.
  `:date` date in YYYYMMDD format; can also be sample
  `:format` csv or psv, default is json
  `:token` IEX API token. If you have been permissioned for CUSIP information
  you’ll receive a CUSIP field, othewise data defaults to exclude CUSIP.
  See https://iextrading.com/developer/docs/#iex-corporate-actions"
  [& {:keys [date] :as params}]
  (api-call (ep "daily-list" "corporate-actions" (if date (str date) ""))
            (select-keys params [:format :token :filter])))

(defn get-iex-dividends
  "Returns dividend information for IEX listed-securities.
  Parameters are optional and should be specified as key value pairs.
  `:date` date in YYYYMMDD format; can also be sample
  `:format` csv or psv, default is json
  `:token` IEX API token. If you have been permissioned for CUSIP information
  you’ll receive a CUSIP field, othewise data defaults to exclude CUSIP.
  See https://iextrading.com/developer/docs/#iex-dividends"
  [& {:keys [date] :as params}]
  (api-call (ep "daily-list" "dividends" (if date (str date) ""))
            (select-keys params [:format :token :filter])))

(defn get-iex-next-day-ex-date
  "Returns advance dividend declarations for IEX-listed securities.
  Parameters are optional and should be specified as key value pairs.
  `:date` date in YYYYMMDD format; can also be sample
  `:format` csv or psv, default is json
  `:token` IEX API token. If you have been permissioned for CUSIP information
  you’ll receive a CUSIP field, othewise data defaults to exclude CUSIP.
  See https://iextrading.com/developer/docs/#iex-next-day-ex-date"
  [& {:keys [date] :as params}]
  (api-call (ep "daily-list" "next-day-ex-date" (if date (str date) ""))
            (select-keys params [:format :token :filter])))

(defn get-iex-listed-symbols
  "Returns IEX-listed securities.
  Parameters are optional and should be specified as key value pairs.
  `:date` date in YYYYMMDD format; can also be sample
  `:format` csv or psv, default is json
  `:token` IEX API token. If you have been permissioned for CUSIP information
  you’ll receive a CUSIP field, othewise data defaults to exclude CUSIP.
  See https://iextrading.com/developer/docs/#iex-listed-symbol-directory"
  [& {:keys [date] :as params}]
  (api-call (ep "daily-list" "symbol-directory" (if date (str date) ""))
            (select-keys params [:format :token :filter])))

