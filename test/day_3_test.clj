(ns day-3-test
  (:require [day-3 :as sut]
            [clojure.test :refer [deftest testing is]]
            [clojure.string :as str]))

(def known-lines
  (str/split-lines
    "..##.......\n#...#...#..\n.#....#..#.\n..#.#...#.#\n.#...##..#.\n..#.##.....\n.#.#.#....#\n.#........#\n#.##...#...\n#...##....#\n.#..#...#.#"))

(def known-map
  (sut/lines->map known-lines))


(deftest trees-on-slope-test
  (is (= 7 (sut/trees-on-slope known-map [3 1]))))
