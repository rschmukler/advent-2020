(ns day-6
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :as set]))

(def input
  (->> (io/resource "day6_input.txt")
       (io/reader)
       slurp))

(defn input->group-answers
  "Convert a string of input into a sequence of sequences of sets
  of answers by each person."
  [str]
  (for [group-lines (str/split str #"\n\n")]
    (for [person-answers (str/split-lines group-lines)]
      (set person-answers))))

(defn answers-with-any-yes
  "Return answers for which anyone answers with a yes."
  [group]
  (reduce set/union group))

(defn answers-with-all-yes
  "Return answers for which everyone answers with a yes."
  [group]
  (reduce set/intersection group))

(defn solve-using
  "Solve for puzzle answer using the provided input and reducing function"
  [fn input]
  (->> input
       (input->group-answers)
       (map (comp count fn))
       (reduce +)))

(comment
  ;; Part one
  (solve-using answers-with-any-yes input)
  ;; Part one
  (solve-using answers-with-all-yes input))
