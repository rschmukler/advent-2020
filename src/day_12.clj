(ns day-12
  (:require [clojure.java.io :as io]
            [clojure.core.match :refer [match]]))


(defn line->instruction
  "Map a line into an instruction"
  [line]
  [(case (first line)
     \N :north
     \S :south
     \E :east
     \W :west
     \L :left
     \R :right
     \F :forward)
   (read-string (apply str (rest line)))])

(def input
  "Input for day 11"
  (->> (io/resource "day12_input.txt")
       (io/reader)
       (line-seq)
       (mapv line->instruction)))

(defn turn
  "Return the new direction a ship would be facing given turning from the
  `existing-direction` `deg` clockwise."
  [existing-direction deg]
  (->> (cycle [:north :east :south :west])
       (drop-while #(not= % existing-direction))
       (drop 1)
       (take (Math/abs (quot deg 90)))
       (last)))

(defn apply-instruction-part-one
  "Applies the rules from part one."
  [ship instruction]
  (match instruction
    [:forward unit] (apply-instruction-part-one ship [(:direction ship) unit])
    [:left deg]     (update ship :direction turn (- 360 deg))
    [:right deg]    (update ship :direction turn deg)
    [:north unit]   (update ship :north + unit)
    [:south unit]   (update ship :north - unit)
    [:east unit]    (update ship :east + unit)
    [:west unit]    (update ship :east - unit)))

(defn solve-using
  "Solves using the provided instruction function, returning the manhattan distance."
  [f instructions]
  (let [{:keys [east north]} (reduce
                               f
                               {:north          0 :east          0 :direction :east
                                :waypoint-north 1 :waypoint-east 10}
                               instructions)]
    (+ (Math/abs east) (Math/abs north))))

(defn rotate-waypoint
  "Rotates the waypoint for `ship` by the provided `deg` clockwise."
  [ship deg]
  (let [{:keys [waypoint-east waypoint-north]} ship]
    (case deg
      90  (assoc ship :waypoint-east waypoint-north :waypoint-north (- waypoint-east))
      180 (assoc ship :waypoint-east (- waypoint-east) :waypoint-north (- waypoint-north))
      270 (assoc ship :waypoint-east (- waypoint-north) :waypoint-north waypoint-east))))

(defn move-toward-waypoint
  "Return a ship moving toward the state's waypoint `times` times."
  [{:keys [waypoint-north waypoint-east] :as ship} times]
  (-> ship
      (update :north + (* times waypoint-north))
      (update :east + (* times waypoint-east))))


(defn apply-instruction-part-two
  "Apply the instruction using the rules in part two."
  [state instruction]
  (match instruction
    [:forward unit] (move-toward-waypoint state unit)
    [:right deg]    (rotate-waypoint state deg)
    [:left deg]     (rotate-waypoint state (- 360 deg))
    [:north unit]   (update state :waypoint-north + unit)
    [:south unit]   (update state :waypoint-north - unit)
    [:east unit]    (update state :waypoint-east + unit)
    [:west unit]    (update state :waypoint-east - unit)))

(comment
  ;; Solve part one
  (solve-using apply-instruction-part-one input)

  ;; Solve part two
  (solve-using apply-instruction-part-two input))
