(ns day-11-test
  (:require [day-11 :as sut]
            [clojure.test :refer [deftest is]]
            [clojure.string :as str]))

(def input
  (->> (slurp "test/fixtures/day11_test.txt")
       (str/split-lines)
       (mapv vec)))

(def expected-round-two
  (->> (slurp "test/fixtures/day11_round_2.txt")
       (str/split-lines)
       (mapv vec)))

(def expected-round-three
  (->> (slurp "test/fixtures/day11_round_3.txt")
       (str/split-lines)
       (mapv vec)))

(def expected-round-four
  (->> (slurp "test/fixtures/day11_round_4.txt")
       (str/split-lines)
       (mapv vec)))

(def empty-seat-map
  (->> (slurp "test/fixtures/day11_empty_seats.txt")
       (str/split-lines)
       (mapv vec)))


(deftest char-at-test
  (is (= \L (sut/char-at input 0 0)))
  (is (= \# (sut/char-at expected-round-two 0 0))))

(deftest occupied-empty-test?
  (is (= false (sut/occupied-seat? input -1 -1)))
  (is (= false (sut/empty-seat? input -1 -1)))
  (is (= false (sut/occupied-seat? input 0 0)))
  (is (sut/empty-seat? input 0 0))
  (is (sut/occupied-seat? [[\#]] 0 0)))

(deftest adjacent-occupied-count-test
  (is (= 4 (sut/adjacent-occupied-count expected-round-two 0 2))))


(deftest simulate-round-test
  (doseq [[input expected] (partition 2 1 [input
                                           expected-round-two
                                           expected-round-three
                                           expected-round-four])]
    (is (= expected (sut/simulate-round input sut/transition-coordinate-part-one)))))

(deftest solve-part-one-test
  (is (= 37 (sut/solve-using input sut/transition-coordinate-part-one))))

(deftest find-closest-seat-test
  (is (= [0 7] (sut/find-closest-seat empty-seat-map 0 0 #{:right})))
  (is (= [0 7] (sut/find-closest-seat empty-seat-map 1 7 #{:up})))
  (is (= [0 7] (sut/find-closest-seat empty-seat-map 1 6 #{:up :right}))))

(deftest visible-occuped-seat-count-test
  (is (= 8 (sut/visible-occuped-seats-count empty-seat-map 4 3))))
