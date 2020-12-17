(ns day-17-test
  (:require [day-17 :as sut]
            [clojure.test :refer [deftest is]]
            [cuerdas.core :as str]))


(def input
  (-> (slurp "test/fixtures/day17_test.txt")
      (str/lines)
      (sut/input->energy-source-3d)))


(deftest neighbors-test
  (is (= 26 (count (sut/neighbors [0 0 0]))))
  (is (= 80 (count (sut/neighbors [0 0 0 0])))))

(deftest active-neighbors-count-test
  (is (= 1 (sut/active-neighbor-count input [0 0 0])))
  (is (= 1 (sut/active-neighbor-count (sut/turn-to-eleven input) [0 0 0 0]))))

(deftest solve-test
  (is (= 112 (sut/solve input)))
  (is (= 848 (sut/solve input true))))
