(ns day-21
  (:require [cuerdas.core :as str]
            [clojure.java.io :as io]))

(defn line->food
  "Return a sequence of foods from the provided `lines`"
  [line]
  (let [words                     (str/split line " ")
        [ingredients _ allergens] (->> words
                                       (partition-by #{"(contains"}))]
    {:ingredients (set ingredients)
     :allergens   (->> allergens
                       (map #(str/replace % #",|\)" ""))
                       set)}))

(def foods
  "Our food list"
  (->> (io/resource "day21_input.txt")
       (io/reader)
       (line-seq)
       (mapv line->food)))

(defn allergens->ingredient-frequencies
  "Return a map of allergens to maps of foods and their frequencies"
  [foods]
  (->> (for [food foods]
         (for [allergen   (:allergens food)
               ingredient (:ingredients food)]
           [allergen ingredient]))
       (apply concat)
       (reduce
         #(update-in %1 %2 (fnil inc 0))
         {})))

(defn ingredients->allergen-frequencies
  "Return a map of ingredient to maps of allergens and their frequencies"
  [foods]
  (->> (for [food foods]
         (for [allergen   (:allergens food)
               ingredient (:ingredients food)]
           [ingredient allergen]))
       (apply concat)
       (reduce
         #(update-in %1 %2 (fnil inc 0))
         {})))

(defn top-candidates
  "Return the top candidates for the provided ingredient freuqencies"
  [ingredient-freqs]
  (let [max-count (->> ingredient-freqs vals (apply max))]
    (->> ingredient-freqs
         (filter (comp #{max-count} val))
         (keys)
         (set))))

(defn deduce-ingredient
  [allergen ingredient->allergen-freqs allergen->ingredient-freqs]
  (->> allergen
       (allergen->ingredient-freqs)
       (top-candidates)
       (some
         (fn [ingredient]
           (when (->> (ingredient->allergen-freqs ingredient)
                      (keys)
                      (remove #{allergen})
                      (filter #(-> %
                                   allergen->ingredient-freqs
                                   top-candidates
                                   (contains? ingredient)))
                      (empty?))
             ingredient)))))


(defn ingredients->allergens
  "Solve for knowable ingredients using the provided foods"
  [foods]
  (loop [allergen->ingredient-freqs (allergens->ingredient-frequencies foods)
         ingredient->allergen-freqs (ingredients->allergen-frequencies foods)
         allergens                  (->> allergen->ingredient-freqs
                                         (sort-by (comp count key))
                                         (keys))
         result                     {}]
    (let [allergen (first allergens)
          deduced  (when allergen
                     (deduce-ingredient allergen
                                        ingredient->allergen-freqs
                                        allergen->ingredient-freqs))
          done?    (empty? allergen->ingredient-freqs)]
      (cond
        done?                result
        deduced              (recur
                               (dissoc allergen->ingredient-freqs allergen)
                               (->> ingredient->allergen-freqs
                                    (map (juxt key #(dissoc (val %) allergen)))
                                    (into {}))
                               (rest allergens)
                               (assoc result deduced allergen))
        (and allergen
             (nil? deduced)) (recur
                               allergen->ingredient-freqs
                               ingredient->allergen-freqs
                               (rest allergens)
                               result)
        (not done?)          (recur allergen->ingredient-freqs
                                    ingredient->allergen-freqs
                                    (keys allergen->ingredient-freqs)
                                    result)))))

(defn solve-part-one
  "Solve part one"
  [foods]
  (let [known-ingredients (->> foods
                               (ingredients->allergens)
                               (keys)
                               (set))]
    (->> foods
         (mapcat :ingredients)
         (remove known-ingredients)
         count)))

(defn solve-part-two
  "Solve part two"
  [foods]
  (->> foods
       (ingredients->allergens)
       (sort-by val)
       (keys)
       (str/join ",")))

(comment
  (solve-part-one foods)
  (solve-part-two foods))
