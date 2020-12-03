(ns day-3-test
  (:require [day-3 :as sut]
            [clojure.test :refer [deftest testing is]]
            [clojure.string :as str]))

(def known-map
  (->> (str/split-lines
         "..##.......\n#...#...#..\n.#....#..#.\n..#.#...#.#\n.#...##..#.\n..#.##.....\n.#.#.#....#\n.#........#\n#.##...#...\n#...##....#\n.#..#...#.#")
       (to-array-2d)))

(deftest trees-on-slope-test
  (is (= 7 (sut/trees-on-slope known-map {:x-delta 3 :y-delta 1}))))
