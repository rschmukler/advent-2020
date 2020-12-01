(ns day-1
  "Day 1, here we come"
  (:require [clojure.java.io :as io]))

(defn combinations
  "Select all unique combinations of `n` items from `coll`."
  [n coll]
  (loop [iterations 0
         combos     #{#{}}]
    (if (== iterations n)
      combos
      (recur (inc iterations)
             (set (for [x     coll
                        combo combos
                        :when (not (contains? combo x))]
                    (conj combo x)))))))

(defn find-n-with-sum
  "Finds `n` numbers that sum up to `sum` from `coll`"
  [n sum coll]
  (->> (combinations n coll)
       (filter #(= sum (apply + %)))
       (first)))

(def problem-input
  (->> (io/resource "day1_input.txt")
       (io/reader)
       (line-seq)
       (map read-string)))

(defn solve-part-1
  "Solve day-1.part-1"
  []
  (->> problem-input
       (find-n-with-sum 2 2020)
       (apply *)))

(defn solve-part-2
  "Solve day-1.part-2"
  []
  (->> problem-input
       (find-n-with-sum 3 2020)
       (apply *)))

(comment
  (solve-part-1)
  (solve-part-2))
