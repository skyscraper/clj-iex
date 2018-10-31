(ns clj-iex.stock
  (:require [clj-iex.api :refer [api-call]]))

(defonce m "market")
(defonce range-options #{"5y" "2y" "1y" "ytd" "6m" "3m" "1m" "1d" "dynamic"})

(def ep (partial conj ["stock"]))

(defn batch-request
  "Returns data in batch, depending on types, symbols, and range.
  Parameters are key-value pairs as indicated below.
  `:types` required; list of endpoints as strings or keywords, min 1, max 10
  `:symbols` optional; list of symbols as strings or keywords, max 100;
  if empty, market will be used
  `:range` optional, but required if chart is in `:types`; chart range
  Additional parameters can also be specified as key value pairs.
  See https://iextrading.com/developer/docs/#batch-requests for further info."
  [& {:keys [symbols] :as params}]
  (let [sym (if (= 1 (count symbols)) (first symbols) m)]
    (api-call (ep sym "batch") params)))

(defn get-book
  "Returns the book for a given symbol.
  `sym` required; symbol"
  [sym & {:as params}]
  (api-call (ep sym "book") params))

(defn get-chart
  "Returns chart data for a given symbol.
  `sym` required; symbol
  Remaining parameters are optional key-value pairs
  `:range` available values: range-options or date in YYYYMMDD format
  `:chartReset` boolean. If true, 1d chart will reset at midnight instead of the
  default behavior of 9:30am ET.
  `:chartSimplify` boolean. If true, runs a polyline simplification using the
  Douglas-Peucker algorithm. This is useful if plotting sparkline charts.
  `:chartInterval` number. If passed, chart data will return every Nth element
  as defined by chartInterval
  `:changeFromClose` boolean. If true, changeOverTime and marketChangeOverTime
  will be relative to previous day close instead of the first value.
  `:chartLast` number. If passed, chart data will return the last N elements
  See https://iextrading.com/developer/docs/#chart for further info."
  [sym & {:as params}]
  (let [r (cond (or (not (contains? params :range))
                    (nil? (:range params))
                    (= "" (:range params))) ""
                (contains? range-options (:range params)) (:range params)
                :else (str "date/" (:range params)))]
    (api-call (ep sym "chart" r) (dissoc params :range))))

(defn get-quotes-for-sector
  "Returns a list of quote objects for a given sector.
  `:collectionName` required kvp; sector
  See https://iextrading.com/developer/docs/#collections for further info."
  [& {:as params}]
  (api-call (ep m "collection" "sector") params))

(defn get-quotes-for-tag
  "Returns a list of quote objects for a given tag.
  `:collectionName` required kvp; tag
  See https://iextrading.com/developer/docs/#collections for further info."
  [{:as params}]
  (api-call (ep m "collection" "tag") params))

(defn get-quotes-for-list
  "Returns a list of quote objects for a given list.
  `:collectionName` required kvp; list
  See https://iextrading.com/developer/docs/#collections for further info."
  [& {:as params}]
  (api-call (ep m "collection" "list") params))

(defn get-company
  "Returns company information for a given symbol.
  `sym` required; symbol"
  [sym & {:as params}]
  (api-call (ep sym "company") params))

(defn get-crypto-quotes
  "Returns a list of quotes for all cryptocurrencies supported by the IEX API."
  [& {:as params}]
  (api-call (ep m "crypto") params))

(defn get-delayed-quote
  "Returns the 15 minute delayed quote for a given symbol.
  `sym` required; symbol"
  [sym & {:as params}]
  (api-call (ep sym "delayed-quote") params))

(defn get-dividends
  "Returns dividend info for a given symbol.
  `sym` required; symbol
  `time-range` required; options are: 5y, 2y, 1y, ytd, 6m, 3m, and 1m.
  See https://iextrading.com/developer/docs/#dividends for further info."
  [sym time-range & {:as params}]
  (api-call (ep sym "dividends" time-range) params))

(defn get-earnings
  "Returns the last 4 quarters worth of earnings data for a given symbol.
  `sym` required; symbol"
  [sym & {:as params}]
  (api-call (ep sym "earnings") params))

(defn get-earnings-today
  "Returns earnings that will be announced today
  Includes earnings both before market open and after market close."
  [& {:as params}]
  (api-call (ep m "today-earnings") params))

(defn get-effective-spread
  "Returns the effective spread for a symbol.
  `sym` required; symbol
  Per the IEX API doc: This returns an array of effective spread, eligible
  volume, and price improvement of a stock, by market.
  See https://iextrading.com/developer/docs/#effective-spread for futher info."
  [sym & {:as params}]
  (api-call (ep sym "effective-spread") params))

(defn get-financials
  "Returns income statement, balance sheet, and cash flow data.
  `sym` required; symbol
  `period` optional; valid values are quarter and annual"
  [sym & {:as params}]
  (api-call (ep sym "financials") params))

(defn get-upcoming-ipos
  "Returns a list of upcoming IPOs for the current and next month."
  [& {:as params}]
  (api-call (ep m "upcoming-ipos") params))

(defn get-today-ipos
  "Returns a list of upcoming IPOs for today."
  [& {:as params}]
  (api-call (ep m "today-ipos") params))

(defn get-threshold-securities
  "Returns Reg-SHO threshold securities.
  Per IEX API: IEX-listed securities that have an aggregate fail to deliver
  position for five consecutive settlement days at a registered clearing agency,
  totaling 10,000 shares or more and equal to at least 0.5% of the issuer’s
  total shares outstanding (i.e., 'threshold securities').
  Parameters are all optional and should be specified key value pairs
  `:date` YYYYMMDD format or sample
  `:format` csv or psv, default is json
  `:token` IEX API token. If you have been permissioned for CUSIP information
  you’ll receive a CUSIP field, othewise data defaults to exclude CUSIP.
  See https://iextrading.com/developer/docs/#iex-regulation-sho-threshold-securities-list
  for further info."
  [& {:keys [date] :as params}]
  (api-call (ep m "threshold-securities" (if date (str date) "")) (dissoc params :date)))

(defn get-short-interest
  "Returns short interest list
  Per IEX API: The consolidated market short interest positions in all
  IEX-listed securities are included in the IEX Short Interest Report.
  Parameters are all optional and should be specified as key value pairs.
  `:sym` symbol; if omitted, market is used
  `:date` date in YYYYMMDD format; can also be sample
  `:format` csv or psv, default is json
  `:token` IEX API token. If you have been permissioned for CUSIP information
  you’ll receive a CUSIP field, othewise data defaults to exclude CUSIP.
  See https://iextrading.com/developer/docs/#iex-short-interest-list"
  [& {:keys [sym date] :as params :or {sym m}}]
  (api-call (ep sym "short-interest" (if date (str date) ""))
            (select-keys params [:format :token :filter])))

(defn get-key-stats
  "Returns key statistics for a given security.
  `sym` required; symbol"
  [sym & {:as params}]
  (api-call (ep sym "stats") params))

(defn get-largest-trades
  "Returns 15 minute delayed, last sale eligible trades.
  `sym` required; symbol"
  [sym & {:as params}]
  (api-call (ep sym "largest-trades") params))

(defn get-most-active
  "Returns an array of quotes for the 10 most active symbols.
  `:displayPercent` boolean, optional kvp, default is false; if true, percentage
  values will be multiplied by 100."
  [& {:as params}]
  (api-call (ep m "list" "mostactive") params))

(defn get-top-gainers
  "Returns an array of quotes for the biggest 10 gainers.
  `:displayPercent` boolean, optional kvp, default is false; if true, percentage
  values will be multiplied by 100."
  [& {:as params}]
  (api-call (ep m "list" "gainers") params))

(defn get-losers
  "Returns an array of quotes for the biggest 10 losers.
  `:displayPercent` boolean, optional kvp, default is false; if true, percentage
  values will be multiplied by 100."
  [& {:as params}]
  (api-call (ep m "list" "losers") params))

(defn get-iex-volume
  "Returns an array of quotes for the top 10 symbols by volume.
  `:displayPercent` boolean, optional, default is false; if true, percentage
  values will be multiplied by 100."
  [& {:as params}]
  (api-call (ep m "list" "iexvolume") params))

(defn get-iex-percent
  "Returns an array of quotes for the top 10 symbols by percent.
  `:displayPercent` boolean, optional, default is false; if true, percentage
  values will be multiplied by 100."
  [& {:as params}]
  (api-call (ep m "list" "iexpercent") params))

(defn get-in-focus
  "Returns an array of quotes for the top 10 symbols in focus.
  `:displayPercent` boolean, optional, default is false; if true, percentage
  values will be multiplied by 100."
  [& {:as params}]
  (api-call (ep m "list" "infocus") params))

(defn get-logo
  "Returns a url for a company logo.
  `sym` required; symbol"
  [sym & {:as params}]
  (api-call (ep sym "logo") params))

(defn get-news
  "Returns news for a symbol or the market
  `:sym` optional kvp; symbol. Default is market
  `:last` optional kvp; number of stories to get"
  [& {:keys [sym last] :as params :or {sym m}}]
  (api-call (ep sym "news" (if last (str "last/" last) ""))
            (dissoc params :sym :last)))

(defn get-ohlc
  "Returns official open, high, low, and close
  `:sym` optional kvp; symbol. Default is market"
  [& {:keys [sym] :as params :or {sym m}}]
  (api-call (ep sym "ohlc") (dissoc params :sym)))

(defn get-peers
  "Returns IEX-defined peers for a given symbol
  `sym` required; symbol"
  [sym & {:as params}]
  (api-call (ep sym "peers") params))

(defn get-previous-day-price-data
  "Returns adjusted price data for the day before
  `:sym` optional kvp; symbol. Default is market"
  [& {:keys [sym] :as params :or {sym m}}]
  (api-call (ep sym "previous") (dissoc params :sym)))

(defn get-price
  "Returns the price for the symbol.
  `sym` required; symbol
  Per the IEX API: A single number, being the IEX real time price, the 15 minute
  delayed market price, or the previous close price, is returned."
  [sym]
  (api-call (ep sym "price")))

(defn get-quote
  "Returns the quote for the symbol.
  `sym` required; symbol
  `:displayPercent` boolean, optional kvp, default is false; if true, percentage
  values will be multiplied by 100"
  [sym & {:as params}]
  (api-call (ep sym "quote") params))

(defn get-relevant
  "Returns peers when available and most active when peers unavailable.
  `sym` required; symbol"
  [sym & {:as params}]
  (api-call (ep sym "relevant") params))

(defn get-sector-performance
  "Returns a list of performance by sector, based on ETF."
  [& {:as params}]
  (api-call (ep m "sector-performance") params))

(defn get-stock-splits
  "Returns historical stock split data.
  `sym` required; symbol
  `range` required; time range, available values are 5y,2y,1y,ytd,6m,3m,1m"
  [sym range & {:as params}]
  (api-call (ep sym "splits" range) params))

(defn get-volume-by-venue
  "Returns volume by venue.
  `sym` required; symbol"
  [sym & {:as params}]
  (api-call (ep sym "volume-by-venue") params))

