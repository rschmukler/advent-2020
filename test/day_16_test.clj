(ns day-16-test
  (:require [day-16 :as sut]
            [clojure.test :refer [deftest testing is]]
            [cuerdas.core :as str]))


(def raw-input
  "Sample input for day 16"
  (slurp "test/fixtures/day16_test.txt"))

(def input
  (sut/parse-input raw-input))

(def input-part-two
  (sut/parse-input (slurp "test/fixtures/day16_test_2.txt")))


(deftest header->fields-test
  (is (= {:class [1 3 5 7]
          :row   [6 11 33 44]
          :seat  [13 40 45 50]}
         (:fields input))))

(deftest scanning-error-rate-test
  (is (= 71 (sut/scanning-error-rate input))))

(deftest ticket->field-matches-test
  (is (= {0 #{:row :seat}
          1 #{:class :row :seat}
          2 #{:class :row :seat}}
         (sut/ticket->field-matches {:class [0 1 4 19]
                                     :row   [0 5 8 19]
                                     :seat  [0 13 16 19]}
                                    [3 9 18]))))


(deftest deduce-ix-test
  (is (= 2 (sut/deduce-ix 3 {1 2 2 3 0 2})))
  (is (nil? (sut/deduce-ix 3 { 1 3 2 3 0 2 }))))

(deftest field-counts->order-test
  (is (= [:row :class :seat]
         (sut/field-counts->order
           {:class { 1 3 2 3 0 2 }
            :row   { 1 3 2 3 0 3 }
            :seat  { 1 2 2 3 0 2 }}))))

(deftest solve-field-order-test
  (is (= [:row :class :seat]
         (sut/solve-field-order input-part-two))))
