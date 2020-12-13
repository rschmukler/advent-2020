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

(defn update-time-generator-for-bus
  "Update the provided time generator to a timestamp that will work provide a valid departure
  for the bus, and increase the iterate step such that all succssive numbers will produce valid
  depatures for the bus (and all previous buses)."
  [time-gen bus]
  (let [step (->> time-gen (take 2) reverse (apply -))]
    (->> time-gen
         (utils/find-first (partial valid-departure? bus))
         (iterate #(+ % (utils/least-common-multiple step (:id bus)))))))

(defn solve-part-two
  [{:keys [buses]}]
  (->> buses
       (reduce update-time-generator-for-bus (range))
       first))

(comment
  (solve-part-one input)
  (solve-part-two input)
  )
