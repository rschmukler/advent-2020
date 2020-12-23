(ns day-23-test
  (:require [day-23 :as sut]
            [clojure.test :refer [deftest testing is]]))

(deftest turn-test
  (is (= [[2 8 9 1 5 4 6 7 3]
          [5 4 6 7 8 9 1 3 2]
          [8 9 1 3 4 6 7 2 5]
          [4 6 7 9 1 3 2 5 8]
          [1 3 6 7 9 2 5 8 4]
          [9 3 6 7 2 5 8 4 1]
          [2 5 8 3 6 7 4 1 9]
          [6 7 4 1 5 8 3 9 2]
          [5 7 4 1 8 3 9 2 6]
          [8 3 7 4 1 9 2 6 5]]
         (->> (iterate sut/turn (sut/->game [3 8 9 1 2 5 4 6 7]))
              (drop 1)
              (take 10)
              (mapv (comp vec (partial sut/cup-seq false)))))))


(deftest solve-part-one-test
  (is (= "92658374" (sut/solve-part-one 10 [3 8 9 1 2 5 4 6 7])))
  (is (= "67384529" (sut/solve-part-one [3 8 9 1 2 5 4 6 7]))))


(deftest solve-part-two-test
  (is (= 149245887792 (sut/solve-part-two [3 8 9 1 2 5 4 6 7]))))
