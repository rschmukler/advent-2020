(ns day-10
  (:require [clojure.java.io :as io]
            [clojure.set :as set]))

(defn input->adapters
  [input]
  (-> input
      (conj 0)
      (conj (+ 3 (apply max input)))
      set))

(def input
  "Input for day 10"
  (->> (io/resource "day10_input.txt")
       (io/reader)
       (line-seq)
       (mapv read-string)
       input->adapters))

(defn solve-part-one
  "Presumably we can go up"
  [input]
  (let [differences (->> input
                         (sort)
                         (partition 2 1)
                         (map #(- (second %) (first %)))
                         (frequencies))]
    (* (get differences 1)
       (get differences 3))))

(def count-paths
  (memoize
    (fn [adapters->neighbors node]
      (let [neighbors (adapters->neighbors node)]
        (->> neighbors
             (map (partial count-paths adapters->neighbors))
             (reduce + (max 0 (dec (count neighbors)))))))))

(defn solve-part-two
  [adapters]
  (let [adapters->neighbors (->> adapters
                                 (map
                                   (juxt identity #(set/intersection adapters
                                                                     (set (range (inc %) (+ % 4))))))
                                 (into {}))]
    (inc (count-paths adapters->neighbors 0))))

(comment
  (solve-part-one input)
  (solve-part-two input))
