(ns day-11
  (:require [clojure.java.io :as io]))

(def input
  "Input for day 11"
  (->> (io/resource "day11_input.txt")
       (io/reader)
       (line-seq)
       (mapv vec)))

(defn char-at
  "Return the character at the provided"
  [a row aisle]
  ((a row) aisle))

(defn in-bounds?
  "Return whether the coordinates at row and aisle are in bounds"
  [a row aisle]
  (and (>= (dec (count a)) row 0)
       (>= (dec (count (first a))) aisle 0)))

(defn occupied-seat?
  "Return whether the provided row aisle coordinate is an occuped seat"
  [a row aisle]
  (and (in-bounds? a row aisle)
       (= (char-at a row aisle) \#)))

(defn empty-seat?
  "Return whether the provided row aisle coordinate is an empty seat"
  [a row aisle]
  (and (in-bounds? a row aisle)
       (= (char-at a row aisle) \L)))

(defn seat?
  "Return whether the provided row aisle coordinate is a seat (empty or otherwise)"
  [a row aisle]
  (and (in-bounds? a row aisle)
       (not= (char-at a row aisle) \.)))

(defn adjacent-occupied-count
  "Return the number of seats around the `row` and `aisle` that are occuped"
  [a row aisle]
  (count (for [y     (range (dec row) (+ row 2))
               x     (range (dec aisle) (+ aisle 2))
               :when (and (not (and (= y row)
                                    (= x aisle)))
                          (occupied-seat? a y x))]
           true)))

(defn transition-coordinate-part-one
  "State transition logic for part one."
  [a row aisle]
  (let [occupied-neighbors (adjacent-occupied-count a row aisle)]
    (cond
      (and (empty-seat? a row aisle)
           (zero? occupied-neighbors)) \#
      (and (occupied-seat? a row aisle)
           (>= occupied-neighbors 4))  \L
      :else                            (char-at a row aisle))))


(defn simulate-round
  "Simulate a round using the provided transition function"
  [a transition-fn]
  (let [max-rows   (count a)
        max-aisles (count (first a))]
    (vec
      (for [row (range max-rows)]
        (vec
          (for [aisle (range max-aisles)]
            (transition-fn a row aisle)))))))

(defn solve-using
  "Solve the puzzle using the provided transition fn"
  [input transition-fn]
  (loop [input input]
    (let [new (simulate-round input transition-fn)]
      (if (= new input)
        (->> input
             flatten
             (filter #{\#})
             count)
        (recur new)))))


;; Part Two

(defn find-closest-seat
  "Find the coordinates of the nearest seat in the provided directions.

  Takes a set of directions #{:left :right :up :down}. Obviously don't use up and down at the
  same time, etc."
  [a row aisle directions]
  (let [iterate-row   (cond
                        (contains? directions :up)   dec
                        (contains? directions :down) inc
                        :else                        identity)
        iterate-aisle (cond
                        (contains? directions :left)  dec
                        (contains? directions :right) inc
                        :else                         identity)]
    (->> (map vector (iterate iterate-row row) (iterate iterate-aisle aisle))
         (drop 1)
         (take-while #(apply in-bounds? a %))
         (some #(when (apply seat? a %) %)))))

(defn visible-occuped-seats-count
  "Return the number visible occuped seats from the provided row and aisle."
  [a row aisle]
  (count (for [row-dir   #{:up :down nil}
               aisle-dir #{:left :right nil}
               :when     (or row-dir aisle-dir)
               :let      [nearest-seat (find-closest-seat a row aisle #{row-dir aisle-dir})]
               :when     (and
                           nearest-seat
                           (apply occupied-seat? a nearest-seat))]
           true)))

(defn transition-coordinate-part-two
  [a row aisle]
  (let [visible-neighbors (visible-occuped-seats-count a row aisle)]
    (cond
      (and (empty-seat? a row aisle)
           (zero? visible-neighbors)) \#
      (and (occupied-seat? a row aisle)
           (>= visible-neighbors 5))  \L
      :else                           (char-at a row aisle))))


(comment
  (solve-using input transition-coordinate-part-one)
  (solve-using input transition-coordinate-part-two))
