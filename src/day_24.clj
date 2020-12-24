(ns day-24
  (:require [cuerdas.core :as str]
            [clojure.java.io :as io]))

(def directions
  {"e"  {:x 2}
   "w"  {:x -2}
   "ne" {:x 1 :y 1}
   "sw" {:x -1 :y -1}
   "nw" {:x -1 :y 1}
   "se" {:x 1 :y -1}})

(defn line->coord
  "Convert a line into a series of directions"
  [line]
  (loop [result {:x 0 :y 0}
         line   line]
    (if-not (seq line)
      result
      (let [dir (some #(when (str/starts-with? line %)
                         %) (keys directions))]
        (recur (merge-with + result (directions dir))
               (subs line (count dir)))))))


(defn lay-tiles
  "Initialize the tile board using the provided coordinates"
  [coords]
  (reduce
    #(update %1 %2 not)
    {}
    coords))

(def neighbors
  "Return the neighbors for the provided tile"
  (memoize
    (fn [loc]
      (for [[_ dir] directions]
        (merge-with + loc dir)))))

(defn flip-tiles
  "Flip the given tiles for a day"
  [tiles]
  (let [expanded-tiles (->> tiles
                            (keep #(when (val %) (key %)))
                            (mapcat neighbors)
                            (concat (keys tiles))
                            distinct)]
    (->> (for [loc  expanded-tiles
               :let [black? (tiles loc false)
                     black-neighbor-count
                     (->> loc
                          (neighbors)
                          (filter tiles)
                          count)]]
           (cond
             (and black?
                  (or (zero? black-neighbor-count)
                      (> black-neighbor-count 2))) [loc false]
             (and (not black?)
                  (= 2 black-neighbor-count))      [loc true]
             :else                                 [loc black?]))
         (into tiles))))

(def input
  (->> (io/resource "day24_input.txt")
       (io/reader)
       (line-seq)
       (mapv line->coord)))


(comment
  ;; Solve part one:
  (->> (lay-tiles input)
       (filter (comp true? val))
       (count))


  ;; Solve part two:
  (->> (lay-tiles input)
       (iterate flip-tiles)
       (drop 100)
       (first)
       (filter val)
       (count))
  )
