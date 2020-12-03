(ns day-3
  "Day 3, ahoy!"
  (:require [clojure.java.io :as io]))

(defn lines->map
  "Convert a line to a map entry"
  [lines]
  (->> (for [[line-ix line] (map-indexed vector lines)]
         (for [[char-ix char] (map-indexed vector line)]
           [[char-ix line-ix] (case char
                                \. false
                                \# true)]))
       (apply concat)
       (into {})))

(def input
  "Day 3s input"
  (->> (io/resource "day3_input.txt")
       (io/reader)
       (line-seq)
       (lines->map)))

(defn trees-on-slope
  "Find the provided trees on the given slope"
  [input-map [x-delta y-delta]]
  (let [max-x (->> input-map
                   (keys)
                   (map first)
                   (apply max)
                   inc)]
    (loop [[x y] [0 0]
           count 0]
      (if-some [tree? (get input-map [(mod x max-x) y])]
        (recur [(+ x x-delta) (+ y y-delta)] (if tree? (inc count) count))
        count))))

(defn find-slope-product
  "Find the product of all the trees on the provided slope candidates"
  [input-map candidates]
  (->> candidates
       (map (partial trees-on-slope input-map))
       (apply *)))

(comment
  (trees-on-slope input [3 1])
  (find-slope-product input [[1 1]
                             [3 1]
                             [5 1]
                             [7 1]
                             [1 2]]))
