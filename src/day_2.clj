(ns day-2
  "Day 2 AOC 2020"
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn line->policy-check
  "Convert a line of text into a password policy"
  [line]
  (let [[min-max-txt letter-txt passwd] (str/split line #" ")

        [min max] (->> (str/split min-max-txt #"-")
                       (map read-string))]
    {:min      min
     :max      max
     :letter   (first letter-txt)
     :password passwd}))

(defn valid-part-one-policy?
  "Return whether the the provided policy check is valid, per our understanding
  in part one."
  [{:keys [min max letter password]}]
  (let [freq (get (frequencies password) letter 0)]
    (and (<= min freq max))))


(defn valid-part-two-policy?
  "Return whether the provided policy check is valid, per our understanding
  in part two."
  [{:keys [min max letter password]}]
  (let [ix->letter  (vec password)
        min-letter? (= letter (ix->letter (dec min)))
        max-letter? (= letter (ix->letter (dec max)))]
    (or (and min-letter? (not max-letter?))
        (and max-letter? (not min-letter?)))))

(def input
  "Our input, mapped to policy checks"
  (->> (io/resource "day2_input.txt")
       (io/reader)
       (line-seq)
       (mapv line->policy-check)))

(defn solve-part-one
  "Solve part one"
  []
  (->> input
       (filter valid-part-one-policy?)
       (count)))

(defn solve-part-two
  "Solve part two"
  []
  (->> input
       (filter valid-part-two-policy?)
       (count)))


(comment
  (solve-part-one)
  (solve-part-two))
