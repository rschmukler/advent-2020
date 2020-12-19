(ns day-19
  (:require [cuerdas.core :as str]
            [clojure.java.io :as io]
            [clojure.core.match :refer [match]]))

(defn line->rule
  "Convert a line into a rule"
  [line]
  (let [[id rule]     (str/split line ":")
        literal       (re-find #"[a-z]" rule)
        str->seq-expr #(some->> (re-seq #"\d+" %)
                                (map read-string)
                                (into [:seq]))
        or-parts      (str/split rule "|")]
    [(read-string id)
     (cond
       literal                [:literal literal]
       (> (count or-parts) 1) (->> or-parts
                                   (map str->seq-expr)
                                   (into [:or]))
       :else                  (str->seq-expr rule))]))

(defn process-file
  "Return a map of :rules and :messages"
  [path]
  (let [raw-input        (-> (io/file path)
                             (io/reader)
                             slurp)
        [rules messages] (str/split raw-input "\n\n")]
    {:rules    (->> rules
                    (str/lines)
                    (map line->rule)
                    (into {}))
     :messages (str/lines messages)}))

(def raw-input
  (io/resource "day19_input.txt"))

(def processed-input
  (process-file raw-input))

(def rules
  "Our input corced into a rules map"
  (:rules processed-input))

(def messages
  "Our messages"
  (:messages processed-input))

(defn substrings
  "Return all possible substrings of `s`"
  [s]
  (for [x (range 1 (inc (count s)))]
    (subs s 0 x)))

(def match-rule
  "Returns `s` iff it is a match for `rule`"
  (memoize
    (fn [rules rule s]
      (match rule
        [:literal x]    (when (= x s) x)
        [:seq]          (when (= s "") "")
        [:seq id & ids] (->> (substrings s)
                             (some
                               (fn [sub]
                                 (when (and (match-rule rules (get rules id) sub)
                                            (match-rule rules (into [:seq] ids) (subs s (count sub))))
                                   s))))
        [:or a b] (or (match-rule rules a s)
                      (match-rule rules b s))))))
(defn count-matches
  "Return the number of matching messages using the provided rules"
  [rules messages]
  (->> messages
       (filter #(match-rule rules (get rules 0) %))
       count))


(comment
  ;; Solve Part one
  (time
    (count-matches rules messages))

  ;; Solve Part Two
  (let [new-rules (->> ["8: 42 | 42 8"
                        "11: 42 31 | 42 11 31"]
                       (map line->rule)
                       (into rules))]
    (count-matches new-rules messages)))
