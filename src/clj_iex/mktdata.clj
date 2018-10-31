(ns clj-iex.mktdata
  (:require [clj-iex.api :refer [api-call]]))

(def tops ["tops"])
(def hist ["hist"])
(def deep ["deep"])
(def epd (partial conj deep))

(defn get-tops
  "Returns IEX’s aggregated best quoted bid and offer position in near real time.
  `:symbols` optional; symbols to use, defaults is market
  `:format` optional; can only be csv, default is json; "
  [& {:as params}]
  (api-call tops params))

(defn get-last
  "Returns trade data for executions on IEX.
  `:symbols` optional; symbols to use, defaults is market
  `:format` optional; can only be csv, default is json; "
  [& {:as params}]
  (api-call (conj tops "last") params))

(defn get-hist
  "Returns link to IEX data products for download on a T+1 basis.
  `:date` optional; date in YYYYMMDD format, default is for all days available."
  [& {:as params}]
  (api-call hist params))

(defn get-deep
  "Returns real-time depth of book quotations direct from IEX.
  `sym` required; symbol
  See https://iextrading.com/developer/docs/#deep for further info."
  [sym & {:as params}]
  (api-call deep (assoc params :symbols sym)))

(defn get-book
  "Returns IEX’s bids and asks for given symbols.
  `:symbols` required; list of symbols, min=1, max=10
  See https://iextrading.com/developer/docs/#book60 for further info."
  [& {:as params}]
  (api-call (epd "book") params))

(defn get-trades
  "Returns trade report messages on IEX.
  `:symbols` required; list of symbols, min=1, max=10
  `:last` optional; number to return, max=500, default=20
  See https://iextrading.com/developer/docs/#trades for further info."
  [& {:as params}]
  (api-call (epd "trades") params))

(defn get-system-events
  "Returns system events on IEX.
  See https://iextrading.com/developer/docs/#system-event for further info."
  [& {:as params}]
  (api-call (epd "system-event") params))

(defn get-trading-status
  "Returns trading status of one or more securities.
  `:symbols` required; list of symbols, min=1, max=10
  See https://iextrading.com/developer/docs/#trading-status for further info."
  [& {:as params}]
  (api-call (epd "trading-status") params))

(defn get-op-halt-status
  "Returns operational halt status of one or more securities.
  `:symbols` required; list of symbols, min=1, max=10
  See https://iextrading.com/developer/docs/#operational-halt-status for further info."
  [& {:as params}]
  (api-call (epd "op-halt-status") params))

(defn get-short-sale-price-test-status
  "Returns short sale price test status of one or more securities.
  `:symbols` required; list of symbols, min=1, max=10
  See https://iextrading.com/developer/docs/#short-sale-price-test-status for further info."
  [& {:as params}]
  (api-call (epd "ssr-status") params))

(defn get-security-event
  "Returns security events for one or more securities.
  `:symbols` required; list of symbols, min=1, max=10
  See https://iextrading.com/developer/docs/#security-event for further info."
  [& {:as params}]
  (api-call (epd "security-event") params))

(defn get-trade-breaks
  "Returns trade breaks for one or more securities.
  `:symbols` required; list of symbols, min=1, max=10
  `:last` optional; number to return, max=500, default=20
  See https://iextrading.com/developer/docs/#trade-break for further info."
  [& {:as params}]
  (api-call (epd "trade-breaks") params))

(defn get-auction-info
  "Returns IEX-listed security auction information.
  `:symbols` required; list of symbols, min=1, max=10
  See https://iextrading.com/developer/docs/#auction for further info."
  [& {:as params}]
  (api-call (epd "auction") params))


(defn get-official-price
  "Returns the IEX Official Opening and Closing Prices.
  `:symbols` required; list of symbols, min=1, max=10
  See https://iextrading.com/developer/docs/#official-price for further info."
  [& {:as params}]
  (api-call (epd "official-price") params))

