(ns day-7-test
  (:require [day-7 :as sut]
            [clojure.test :refer [deftest testing is]]
            [clojure.string :as str]))

(def input
  (->> (slurp "test/fixtures/day7_test.txt")
       (str/split-lines)
       (map sut/line->contents)
       (into {})))

(def input-part-2
  (->> (slurp "test/fixtures/day7_test_2.txt")
       (str/split-lines)
       (map sut/line->contents)
       (into {})))

(deftest line->contents-test
  (is (= ["light red" {"bright white" 1
                       "muted yellow" 2}]
         (sut/line->contents "light red bags contain 1 bright white bag, 2 muted yellow bags.")))
  (is (= ["faded blue" {}]
         (sut/line->contents "faded blue bags contain no other bags."))))

(deftest solve-part-one-test
  (is (= 4
         (count (sut/bags-containing-child input "shiny gold")))))

(deftest solve-part-two-test
  (is (= 126
         (count (sut/child-bags input-part-2 "shiny gold")))))
