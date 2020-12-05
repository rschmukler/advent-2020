(ns day-4
  "If you were a day, which day would you be?"
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [malli.transform :as mt]
            [malli.core :as m]))

(defn- parse-passport
  [group]
  (when (not= '("") group)
    (->> group
         (mapcat #(str/split % #" "))
         (map #(str/split % #":"))
         (into {}))))

(defn lines->passports
  "Convert a line to a passport"
  [lines]
  (->> lines
       (partition-by #(= "" %))
       (keep parse-passport)))

(def input
  "Day 4s input"
  (->> (io/resource "day4_input.txt")
       (io/reader)
       (line-seq)
       (lines->passports)))

(def schema
  [:map
   ["cid" {:optional true} string?]
   ["byr" [:int {:min 1920
                 :max 2002}]]
   ["eyr" [:int {:min 2020
                 :max 2030}]]
   ["iyr" [:int {:min 2010
                 :max 2020}]]
   ["hgt" [:and
           [:tuple
            {:decode/string #(-> (re-find #"^(\d+)(cm|in)$" %) rest vec)}
            :int
            [:enum "cm" "in"]]
           [:fn (fn [[num unit]]
                  (case unit
                    "cm" (<= 150 num 193)
                    "in" (<= 59 num 76)))]]]
   ["hcl" [:and :string [:re "^#([a-f0-9]{6})$"]]]
   ["ecl" [:enum "amb" "blu" "brn" "gry" "grn" "hzl" "oth"]]
   ["pid" [:and :string [:re #"^\d{9}$"]]]])

(def decode-passport
  "Malli decoder for a passport"
  (m/decoder schema (mt/string-transformer)))

(def explain-passport
  "Malli explainer for a passport"
  (m/explainer schema))

(defn valid-passport-part-one?
  "Return whether the passport is valid per the requirements in part one"
  [passport]
  (->> (explain-passport passport)
       :errors
       (filter (comp #{:malli.core/missing-key} :type))
       (empty?)))

(defn valid-passport-part-two?
  "Return whether the passport is valid per the requirements in part two"
  [passport]
  (-> passport
      (decode-passport)
      (explain-passport)
      (nil?)))

(comment
  ;; Problem 1
  (->> input
       (filter valid-passport-part-one?)
       (count))

  ;; Problem 2
  (->> input
       (filter (every-pred valid-passport-part-two?))
       (count)))
