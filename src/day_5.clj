(ns day-5
  "Cinco"
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def input
  "Day 5s input"
  (->> (io/resource "day5_input.txt")
       (io/reader)
       (line-seq)))


(defn line->seat
  "Decodes a line"
  [line]
  (let [row    (-> (subs line 0 7)
                   (str/replace "F" "0")
                   (str/replace "B" "1")
                   (Integer/parseInt 2))
        column (-> (subs line 7 10)
                   (str/replace "R" "1")
                   (str/replace "L" "0")
                   (Integer/parseInt 2))]
    {:row row :column column :id (+ (* row 8) column)}))

(defn find-missing-seat
  "Find our seat"
  [seats]
  (let [seen-ids (set (map :id seats))]
    (->> (range (apply max seen-ids))
         (filter (every-pred
                   (comp not seen-ids)
                   (comp seen-ids inc)
                   (comp seen-ids dec)))
         (first))))

(def seats
  "Our seats"
  (map line->seat input))

(comment
  ;; Solve part 1
  (->> seats
       (map :id)
       (apply max))

  (find-missing-seat seats))
