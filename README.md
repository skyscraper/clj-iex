# clj-iex

A Clojure library to retrieve stock data from the IEX API.

## Attribution

Data provided for free by [IEX](https://iextrading.com/developer). View [IEX’s Terms of Use](https://iextrading.com/api-exhibit-a/).

## Why?

Because IEX is awesome and provides free data and a relatively high rate-limit.

Per their [usage documentation](https://iextrading.com/developer/docs/#usage):
>We throttle endpoints by IP, but you should be able to achieve over 100 requests per second.

## Installation

With Leiningen or Boot:
```clojure
[clj-iex "0.1.0"]
```

## Usage

This library provides a function for each of the IEX-provided endpoints, and is namespaced based on their documentation. Examples below use `clj-iex.stock` but `clj-iex.refdata`, `clj-iex.mktdata`, `clj-iex.stats`, and `clj-iex.markets` are also available. Effort has been made to briefly document each function and the required/optional parameters, but IEX's documentation should be the primary resource.

To get started, include in your namespace:
```clojure
(ns example.ns
  (:require [clj-iex.stock :as stock]))
```

If we want to get the financials of IBM:

```clojure
(stock/get-financials "ibm")
```

All input parameters (except where specified) support keywords or strings.

```clojure
(stock/get-financials :ibm)
```

Also note that every function in this library supports the optional `filter` parameter, in which the list of fields specified will be the only ones returned in the response.

```clojure
(stock/get-financials :ibm :filter [:netIncome :operatingExpense])
```

There's even a crypto quote endpoint:
```clojure
(get-crypto-quotes)
```

## License

Copyright © 2018 Daniel Lee

Distributed under the MIT License.
