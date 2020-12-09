(ns day-8
  "The ocho"
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.core.match :refer [match]]))

(defn line->instruction
  "Parse a line into an instruction"
  [line]
  (let [[instruction x] (str/split line #" ")]
    [(keyword instruction) (read-string x)]))

(def input
  "Instructions"
  (->> (io/resource "day8_input.txt")
       (io/reader)
       (line-seq)
       (mapv line->instruction)))


(defn run-program
  "Runs a program using the provided instructions, and returns a tuple of the counter and whether we
  terminated gracefully."
  [instructions]
  (loop [seen #{}
         ptr  0
         acc  0]
    (cond
      (contains? seen ptr)         [acc false]
      (= ptr (count instructions)) [acc true]
      :else
      (match (instructions ptr)
        [:nop _] (recur (conj seen ptr) (inc ptr) acc)
        [:jmp x] (recur (conj seen ptr) (+ ptr x) acc)
        [:acc x] (recur (conj seen ptr) (inc ptr) (+ acc x))))))

(defn fix-non-terminating-program
  "Attempts to fix a non terminating program by swapping instructions until
  it finds a terminating program. Returns the count, or nil if it couldn't be solved."
  [instructions remap]
  (let [swap-ixs (for [[ix [op _]] (map-indexed vector instructions)
                       :when       (contains? remap op)]
                   ix)]
    (loop [[ix :as attempts] (reverse swap-ixs)]
      (let [new-program          (update-in instructions [ix 0] remap)
            [result terminated?] (run-program new-program)]
        (cond
          terminated?    result
          (seq attempts) (recur (rest attempts))
          :else          nil)))))

(comment
  (first (run-program input))
  (fix-non-terminating-program input {:nop :jmp
                                      :jmp :nop}))
