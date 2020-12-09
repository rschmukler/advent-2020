(ns day-9-test
  (:require [day-9 :as sut]
            [clojure.test :refer [deftest is]]
            [clojure.string :as str]))

(def input
  (->> (slurp "test/fixtures/day9_test.txt")
       (str/split-lines)
       (mapv read-string)))

(deftest solve-part-one-test
  (is (= 127 (sut/solve-part-one input 5))))

(deftest find-summing-to
  (is (= '() (sut/find-summing-to 0 [1 2 3])))
  (is (= '(1 2) (sut/find-summing-to 3 [1 2 3]))))

(deftest solve-part-two-test
  (is (= 62 (sut/solve-part-two input 5))))
