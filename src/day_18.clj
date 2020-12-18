(ns day-18
  (:require [clojure.java.io :as io]
            [clojure.core.match :refer [match]]))

(def input
  "Puzzle input"
  (-> (io/resource "day18_input.txt")
      (io/reader)
      line-seq))

(defn parse-expr
  "Parse an expression into a list"
  [s]
  (read-string (str "(" s ")")))

(defn eval-expr
  "Evaluate an expression using part one rules"
  [expr]
  (if (number? expr)
    expr
    (->> expr
         (partition 2 2 nil)
         (reduce
           (fn [acc [x op]]
             (let [x   (eval-expr x)
                   val (match (:pending-op acc)
                         nil x
                         '+  (+ (:val acc) x)
                         '*  (* (:val acc) x))]
               {:val        val
                :pending-op op}))
           {:val        nil
            :pending-op nil})
         :val)))

(defn eval-str
  "Evaluate a string expression using our new math rules."
  [s]
  (-> s
      parse-expr
      eval-expr))

(defn eval-advanced-expr
  "Evaluate an expression using part two rules"
  [expr]
  (if-not (seqable? expr)
    expr
    (letfn [(simple-expr? [expr]
              (and (= 1 (count expr)) (number? (first expr))))
            (reduce-expr [expr]
              (if (and (seqable? expr) (= 1 (count expr)))
                (first expr)
                expr))]
      (let [[left right] (->> '[* +]
                              (some (fn [op]
                                      (let [split (split-with #(not= % op) expr)]
                                        (when (not= expr (first split))
                                          split)))))
            [[op] right] (split-at 1 right)]
        ((case op
           + +
           * *)
         (eval-advanced-expr (reduce-expr left))
         (eval-advanced-expr (reduce-expr right)))))))

(defn eval-advanced-str
  "Evaluate a string expression using our new and advanced math rules."
  [s]
  (-> s
      parse-expr
      eval-advanced-expr))



(comment
  ;; Solve for part one
  (->> input
       (map eval-str)
       (reduce +))


  ;; Solve for part two
  (->> input
       (map eval-advanced-str)
       (reduce +))
  )
