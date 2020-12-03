(ns day-3
  "Day 3, ahoy!"
  (:require [clojure.java.io :as io]))

(def input
  "Day 3s input"
  (->> (io/resource "day3_input.txt")
       (io/reader)
       (line-seq)))

(defn trees-on-slope
  "Return a sequence of [x y] tuples of trees on the given slope."
  [lines {:keys [x-delta y-delta]}]
  (let [relevant-y? #(zero? (mod % y-delta))]
    (for [[y line] (map-indexed vector lines)
          :when    (relevant-y? y)
          :let     [x    (* y x-delta)
                    char (nth (cycle line) x)]
          :when    (= \# char)]
      [x y])))

(defn find-slope-product
  "Find the product of all the trees on the provided slope candidates"
  [lines candidates]
  (->> candidates
       (map #(count (trees-on-slope lines %)))
       (apply *)))

(comment
  (count (trees-on-slope input {:x-delta 3 :y-delta 1}))
  (find-slope-product input [{:x-delta 1 :y-delta 1}
                             {:x-delta 3 :y-delta 1}
                             {:x-delta 5 :y-delta 1}
                             {:x-delta 7 :y-delta 1}
                             {:x-delta 1 :y-delta 2}]))
