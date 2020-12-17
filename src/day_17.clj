(ns day-17
  "Warp speed, engage!"
  (:require [clojure.java.io :as io])
  (:refer-clojure :exclude [cycle]))

(defn input->energy-source-3d
  "Convert the provided input into a 3d energy source"
  [lines]
  (->> (for [[line-ix line] (map-indexed vector lines)]
         (for [[char-ix char] (map-indexed vector line)]
           [[0 line-ix char-ix] (case char
                                  \. false
                                  \# true)]))
       (apply concat)
       (into {})))

(def input
  (-> (io/resource "day17_input.txt")
      (io/reader)
      (line-seq)
      (input->energy-source-3d)))

(defn neighbors
  "Return neighboring coordinates for the provided cube"
  [[z y x w]]
  (for [new-z [(dec z) z (inc z)]
        new-y [(dec y) y (inc y)]
        new-x [(dec x) x (inc x)]
        new-w (if w
                [(dec w) w (inc w)]
                [w])
        :when (not= [new-z new-y new-x new-w] [z y x w])]
    (if w
      [new-z new-y new-x new-w]
      [new-z new-y new-x])))


(defn active-neighbor-count
  "Return the number of active neighbors for the provided `cube` in the `energy-source`"
  [energy-source cube]
  (->> (neighbors cube)
       (filter #(get energy-source %))
       count))

(defn cycle-cube
  "State change logic for a single cube"
  [energy-source cube]
  (let [active?   (get energy-source cube)
        neighbors (active-neighbor-count energy-source cube)]
    (or (and active? (<= 2 neighbors 3))
        (and (not active?) (= 3 neighbors)))))

(defn cycle
  "Run a single cycle over the provided `cube` and return the
  new state."
  [energy-source]
  (->> energy-source
       (keys)
       (mapcat neighbors)
       (distinct)
       (map (juxt identity (partial cycle-cube energy-source)))
       (into {})))

(defn turn-to-eleven
  "Convert a smol 3d energy source into a kick-ass 4d energy-source"
  [energy-source]
  (->> energy-source
       (map (juxt #(conj (key %) 0) val))
       (into {})))

(defn solve
  "Return the puzzle solution"
  ([energy-source] (solve energy-source false))
  ([energy-source four-d?]
   (->> (if four-d?
          (turn-to-eleven energy-source)
          energy-source)
        (iterate cycle)
        (take 7)
        last
        (filter val)
        count)))

(comment
  ;; Solve part one
  (solve input)

  ;; Solve part two
  (solve input true))
