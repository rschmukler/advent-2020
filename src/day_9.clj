(ns day-9
  "Nine Nine Nine Nine Nine Nine!"
  (:require [clojure.java.io :as io]
            [data.deque :as dq]))

(def input
  "Day 9 input"
  (->> (io/resource "day9_input.txt")
       (io/reader)
       (line-seq)
       (mapv read-string)))

(defn valid?
  "Return whether or not the provided number is valid using the supplied preamble.

  Takes a single argument that is a sequence of preamble plus the input
  (ie. what partition gives us)"
  [preamble+number]
  (let [preamble (set (butlast preamble+number))
        number   (last preamble+number)]
    (some
      #(and
         (not= % (/ number 2))
         (contains? preamble %)
         (contains? preamble (- number %)))
      preamble)))

(defn solve-part-one
  "Solve part one using the provided input and preamble length"
  [input preamble-length]
  (->> input
       (partition (inc preamble-length) 1)
       (some #(when-not (valid? %)
                (last %)))))

(defn find-summing-to
  "Finds a contiguious sequence of numbers within input that will add up to sum"
  [sum input]
  (let [input (vec input)
        ;; we could go faster by keeping a running count, but
        ;; this feels so much nicer. I benchmarked the difference and its 45ms without and 14ms with.
        ;; Fast enough for now.
        sum-q #(reduce + 0 %)]
    (loop [ptr   0
           queue (dq/deque)]
      (let [existing-sum (sum-q queue)]
        (cond
          (= ptr (count input)) nil
          (= sum existing-sum)  queue
          (> sum existing-sum)  (recur (inc ptr) (dq/add-last queue (input ptr)))
          (< sum existing-sum)  (recur ptr (dq/remove-first queue)))))))

(defn solve-part-two
  "Solve part two using the provided input and preamble length"
  [input preamble-length]
  (let [answer-one (solve-part-one input preamble-length)]
    (when-some [sum-seq (find-summing-to answer-one input)]
      (+ (apply min sum-seq) (apply max sum-seq)))))

(comment
  ;; Solve part one
  (time
    (solve-part-one input 25))

  (time
    (solve-part-two input 25))

  (solve-part-two input 25))
