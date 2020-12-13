(ns day-13
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [utils]))

(defn read-schedule
  [input]
  (let [[timestamp bus-list] input]
    {:timestamp (read-string timestamp)
     :buses     (->> (str/split bus-list #",")
                     (map-indexed (fn [ix s]
                                    (when (not= s "x")
                                      {:id (read-string s)
                                       :ix ix})))
                     (filter some?))}))


(def input
  "Input for day 13"
  (->> (io/resource "day13_input.txt")
       (io/reader)
       (line-seq)
       (read-schedule)))


(defn bus-departing
  [schedule timestamp]
  (->> (filter #(zero? (rem timestamp %)) (map :id (:buses schedule)))
       first))

(defn solve-part-one
  [schedule]
  (let [[bus mins] (->> (iterate inc (:timestamp schedule))
                        (some #(when-some [bus-id (bus-departing schedule %)]
                                 [bus-id (- % (:timestamp schedule))])))]
    (* bus mins)))

(defn valid-departure?
  "Return whether the provided timestamp is a valid departure (per part two)
  for the provided bus"
  [bus t]
  (zero? (rem (+ t (:ix bus)) (:id bus))))

(defn solve-part-two
  [schedule]
  (loop [[bus & buses] (:buses schedule)
         step          1
         t             0]
    (if-not bus
      t
      (recur
        buses
        (utils/least-common-multiple step (:id bus))
        (->> t
             (iterate #(+ step %))
             (some #(and (valid-departure? bus %) %)))))))
