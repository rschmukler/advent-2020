(ns day-16
  (:require [clojure.java.io :as io]
            [clojure.core.match :refer [match]]
            [cuerdas.core :as str]))

(defn line->field
  [line]
  (let [name (-> line
                 (str/split ":")
                 first
                 str/keyword)
        nums (mapv read-string (re-seq #"\d+" line))]
    [name nums]))

(defn header->fields
  [header]
  (into {} (map line->field header)))

(defn line->ticket
  [line]
  (mapv read-string (re-seq #"\d+" line)))

(defn parse-input
  [input]
  (let [[header my-ticket nearby-tickets] (-> input
                                              (str/split "\n\n")
                                              (#(map str/lines %)))]
    {:fields         (header->fields header)
     :ticket         (line->ticket (second my-ticket))
     :nearby-tickets (mapv line->ticket (rest nearby-tickets))}))

(def input
  "Our coveted day 16 input"
  (->> (io/resource "day16_input.txt")
       (io/reader)
       slurp
       parse-input))

(defn valid-value?
  "Return whether the provided value is valid using the provided
  field definition."
  [[min-a max-a min-b max-b] value]
  (or (<= min-a value max-a)
      (<= min-b value max-b)))

(defn any-valid-value?
  "Return whether the provided value is valid for any field."
  [fields value]
  (some #(valid-value? % value) (vals fields)))


(defn scanning-error-rate
  "Return the scanning error rate for the provided puzzle input
  (ie. solve part one)"
  [input]
  (->> input
       :nearby-tickets
       (flatten)
       (remove #(any-valid-value? (:fields input) %))
       (reduce +)))

(defn valid-ticket?
  "Return whether the provided ticket is invalid using the provided fields"
  [fields ticket]
  (every? #(any-valid-value? fields %) ticket))

(defn ticket->field-matches
  "Return a map from ticket index to sets of valid fields"
  [fields ticket]
  (->> (for [[field-name field] fields
             [ix value]         (map-indexed vector ticket)
             :when              (valid-value? field value)]
         [ix field-name])
       (reduce (fn [acc [ix field-name]]
                 (update acc ix (fnil conj #{field-name}) field-name)) {})))

(defn deduce-ix
  "Given a map of index to counts, return a deducable field, if any is possible"
  [max-count ix->counts]
  (reduce
    (fn [candidate [field match-count]]
      (match [candidate match-count]
        [nil max-count] field
        [_ max-count] (reduced nil)
        [_ _]             candidate))
    nil
    ix->counts))

(defn field-counts->order
  "Given a map in the form of `:field-name {ix count}` return the order of the fields"
  [field-counts]
  (let [max-count (->> field-counts
                       vals
                       (mapcat vals)
                       (apply max))]
    (loop [field->ix    {}
           field-counts field-counts]
      (if-not (seq field-counts)
        (->> field->ix (sort-by val) keys vec)
        (let [[field ix] (some #(when-some [ix (deduce-ix max-count (val %))]
                                  [(key %) ix]) field-counts)]
          (assert field "Could not deduce a field / ix")
          (recur
            (assoc field->ix field ix)
            (->> field-counts
                 (#(dissoc % field))
                 (map (juxt key #(dissoc (val %) ix)))
                 (into {}))))))))

(defn solve-field-order
  "Use the provided puzzle input to solve for the field order"
  [input]
  (->> input
       :nearby-tickets
       (filter #(valid-ticket? (:fields input) %))
       (mapcat #(ticket->field-matches (:fields input) %))
       (mapcat #(for [field (second %)]
                  [(first %) field]))
       (reduce
         (fn [acc [ix field]]
           (update-in acc [field ix] (fnil inc 0)))
         {})
       (field-counts->order)))

(defn solve-part-two
  [input]
  (let [field-order (solve-field-order input)
        ticket      (zipmap field-order
                            (:ticket input))]
    (->> ticket
         (filter #(str/starts-with? (name (key %)) "departure"))
         (vals)
         (reduce *))))

(comment
  (str/starts-with? (name :foo) "foo")
  (scanning-error-rate input)
  (solve-part-two input))
