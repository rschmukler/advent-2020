(ns day-3
  "Day 3, ahoy!"
  (:require [clojure.java.io :as io]))

(def input
  "Day 3s input"
  (->> (io/resource "day3_input.txt")
       (io/reader)
       (line-seq)
       (to-array-2d)))

(defn tree-at-coordinate?
  "Return whether the provided array has a tree at the specified coordinate.

  Handles wrapping across the x coordinate."
  [array {:keys [x y]}]
  (let [max-x (-> array first alength)]
    (case (aget array y (mod x max-x))
      \. false
      \# true)))

(defn apply-slope
  "Apply the given slope to the provided input coordinate"
  [{:keys [x-delta y-delta]} {:keys [x y]}]
  {:x (+ x x-delta) :y (+ y y-delta)})

(defn trees-on-slope
  "Find the provided trees on the given slope"
  [array slope]
  (let [max-y (alength array)]
    (->> {:x 0 :y 0}
         (iterate (partial apply-slope slope))
         (take-while #(< (:y %) max-y))
         (filter (partial tree-at-coordinate? array))
         count)))

(defn find-slope-product
  "Find the product of all the trees on the provided slope candidates"
  [input-map candidates]
  (->> candidates
       (map (partial trees-on-slope input-map))
       (apply *)))

(comment
  (trees-on-slope input {:x-delta 3 :y-delta 1})
  (find-slope-product input [{:x-delta 1 :y-delta 1}
                             {:x-delta 3 :y-delta 1}
                             {:x-delta 5 :y-delta 1}
                             {:x-delta 7 :y-delta 1}
                             {:x-delta 1 :y-delta 2}]))
