(ns clj-iex.api-test
  (:require [clojure.test :refer :all]
            [clj-iex.api :refer :all]))

(deftest s-list-test
  (is (= "basic,test" (s-list "," ["basic" "test"])))
  (is (= "keyword|pipe|test" (s-list \| [:keyword :pipe :test]))))

(deftest get-url-test
  (is (= (str base-url "here/is/an/endpoint") (get-url [:here :is :an :endpoint]))))

(deftest sanitize-params-test
  (is (= {:populated-vector "some,stuff"
          :keyword-value "ibm"
          :string-value "SQ"}
         (sanitize-params {:nil-value nil
                           :empty-vector []
                           :populated-vector [:some :stuff]
                           :keyword-value :ibm
                           :empty-string ""
                           :string-value " SQ "}))))

(deftest get-parse-fn-test
  (is (= [["a" "b" "c"]] ((get-parse-fn "csv") "a,b,c")))
  (is (= {:some "json"} ((get-parse-fn "anything") "{\"some\": \"json\"}"))))

(deftest
  api-call-test
  (let [s :IBM]
    (is (= (name s) (:symbol (api-call [:stock s :company]))))))

