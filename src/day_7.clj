(ns day-7
  "A week, already?"
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))


(defn line->contents
  "Parse a line into the provided bag contents in the form of a tuple
  [bag-color {child-color child-count}]"
  [line]
  (let [words   (str/split line #"\s")
        src-bag (apply str (interpose " " (take 2 words)))]
    [src-bag
     (into
       {}
       (for [[_ qty adj color] (re-seq #"(\d+) (\w+) (\w+)" line)]
         [(str adj " " color) (read-string qty)]))]))


(defn transitively-contains-bag?
  "Returns whether the `src-bag` or any of its child bags contain `bag` using
  the provided `rules` map."
  [rules src-bag bag]
  (let [direct-contents (get rules src-bag)]
    (or
      (contains? direct-contents bag)
      (some
        #(transitively-contains-bag? rules % bag)
        (keys direct-contents)))))

(def input
  "Our input coerced into a rules map"
  (->> (io/resource "day7_input.txt")
       (io/reader)
       (line-seq)
       (map line->contents)
       (into {})))

(defn child-bags
  "Return a sequence of all child bags for the provided src-bag"
  [rules src-bag]
  (flatten
    (for [[child child-count] (get rules src-bag)]
      (repeat child-count
              (cons child (child-bags rules child))))))

(defn bags-containing-child
  "Return a sequence of bags that contains the provided bag"
  [rules bag]
  (->> (keys rules)
       (filter #(transitively-contains-bag? rules % bag))))

(comment
  ;; Solve part one
  (count (bags-containing-child input "shiny gold"))
  ;; Solve part two
  (count (child-bags input "shiny gold")))
