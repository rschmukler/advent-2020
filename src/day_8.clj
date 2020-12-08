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
  "Our input coerced into a rules map"
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
    (let [instruction (instructions ptr)
          new-seen    (conj seen ptr)
          new-acc     (match instruction
                        [:acc x] (+ acc x)
                        _        acc)
          new-ptr     (match instruction
                        [:jmp x] (max (+ ptr x) 0)
                        _        (inc ptr))]
      (cond
        (contains? seen new-ptr)           [acc false]
        (= ptr (dec (count instructions))) [new-acc true]
        :else                              (recur new-seen new-ptr new-acc)))))

(defn fix-non-terminating-program
  "Attempts to fix a non terminating program by swapping instructions until
  it finds a terminating program. Returns the count, or nil if it couldn't be solved."
  [instructions]
  (let [swap-instructions (for [[ix [op x]] (map-indexed vector instructions)
                                :when       (#{:nop :jmp} op)]
                            [ix [(match op
                                   :jmp :nop
                                   :nop :jmp) x]])]
    (loop [[[ix new-instruction] :as attempts] (reverse swap-instructions)]
      (let [new-program          (vec
                                   (concat
                                     (subvec instructions 0 ix)
                                     [new-instruction]
                                     (subvec instructions (inc ix))))
            [result terminated?] (run-program new-program)]
        (cond
          terminated?    result
          (seq attempts) (recur (rest attempts))
          :else          nil)))))

(comment
  (first (run-program input))
  (fix-non-terminating-program input))
