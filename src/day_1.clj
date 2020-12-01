(ns day-1
  "Day 1, here we come"
  (:require [clojure.java.io :as io]))

(defn find-n-with-sum
  "Finds `n` numbers that sum up to `sum` from `coll`"
  [n sum coll]
  (let [find-match (if (= 2 n)
                     #(when-some [match (coll (- sum %))]
                        #{% match})
                     #(when-some [subset (find-n-with-sum (dec n) (- sum %) (disj coll %))]
                        (conj subset %)))]
    (some find-match coll)))

(def problem-input
  (->> (io/resource "day1_input.txt")
       (io/reader)
       (line-seq)
       (map read-string)
       set))

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
  (time
    (solve-part-1))
  (time
    (solve-part-2)))
