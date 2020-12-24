(ns day-24-test
  (:require [day-24 :as sut]
            [clojure.test :refer [deftest testing is are]]
            [cuerdas.core :as str]))

(def input
  (->> (slurp "test/fixtures/day24_test.txt")
       (str/lines)
       (mapv sut/line->coord)))


(deftest line->directions-test
  (is (= {:x -1 :y -1} (sut/line->coord "newsesw")))
  (is (= {:x 1 :y -1} (sut/line->coord "esew")))
  (is (= {:x 0 :y 0} (sut/line->coord "nwwswee"))))


(deftest lay-tiles-test
  (let [tiles (sut/lay-tiles input)]
    (is (= 15 (count tiles)))
    (is (= 10 (count (filter (comp true? val) tiles))))))


(deftest flip-tiles-test
  (are [x y] (= y (->> (iterate sut/flip-tiles (sut/lay-tiles input))
                       (drop x)
                       (map #(count (filter (comp true? val) %)))
                       (first)))
    0   10
    1   15
    2   12
    3   25
    4   14
    5   23
    6   28
    7   41
    8   37
    9   49
    10  37
    20  132
    30  259
    100 2208))
