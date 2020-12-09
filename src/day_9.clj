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
  "Return whether or not the provided number is valid using the supplied preamble"
  [preamble number]
  (some
    #(and
       (not= % (/ number 2))
       (contains? preamble %)
       (contains? preamble (- number %)))
    preamble))

(defn solve-part-one
  "Solve part one using the provided input and preamble length"
  [input preamble-length]
  (->> input
       (partition (inc preamble-length) 1)
       (remove
         (fn [partition]
           (valid? (set (butlast partition)) (last partition))))
       (map last)
       (first)))

(defn find-summing-to
  "Finds a contiguious sequence of numbers within input that will add up to sum"
  [sum input]
  (let [input (vec input)
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
