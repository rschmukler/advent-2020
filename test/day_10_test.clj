(ns day-10-test
  (:require [day-10 :as sut]
            [clojure.test :refer [deftest testing is]]
            [clojure.string :as str]))

(def input
  (->> (slurp "test/fixtures/day10_test.txt")
       (str/split-lines)
       (mapv read-string)
       (sut/input->adapters)))

(deftest solve-part-one-test
  (is (= 220 (sut/solve-part-one input))))

(deftest solve-part-two-test
  (is (= 19208 (sut/solve-part-two input))))
