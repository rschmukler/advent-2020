(ns day-4
  "If you were a day, which day would you be?"
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :as set]))

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
  "Day 3s input"
  (->> (io/resource "day4_input.txt")
       (io/reader)
       (line-seq)
       (lines->passports)))

(defn missing-fields
  "Return a set of missing fields for the provided passport"
  [passport]
  (let [expected #{"byr" "iyr" "eyr" "hgt" "hcl" "ecl" "pid" "cid"}
        found    (set (keys passport))
        missing  (set/difference expected found)]
    missing))

(defn valid-passport?
  "Return whether the provided passport is valid"
  [passport]
  (case (missing-fields passport)
    #{"cid"} true
    #{}      true
    false))

(defn valid-birth-year?
  "Return whether the provided birth year is valid"
  [byr]
  (<= 1920 (read-string byr) 2002))

(defn valid-expiration-year?
  "Return whether the provided expiration year is valid"
  [eyr]
  (<= 2020 (read-string eyr) 2030))

(defn valid-issue-year?
  "Return whether the provided issue year is valid"
  [iyr]
  (<= 2010 (read-string iyr) 2020))

(defn valid-height?
  "Return whether the provided height is valid"
  [height]
  (when-some [[_ num unit] (re-find #"^(\d+)(cm|in)$" height)]
    (let [num (read-string num)]
      (case unit
        "cm" (<= 150 num 193)
        "in" (<= 59 num 76)
        false))))

(defn valid-hair-color?
  "Return whether the provided hair color is valid"
  [hair-color]
  (some? (re-find #"^#([a-f0-9]{6})$" hair-color)))

(defn valid-eye-color?
  "Return whether the provided eye color is valid"
  [eye-color]
  (contains? #{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"} eye-color))

(defn valid-pid?
  "Return whether the provided passport id is valid"
  [pid]
  (some? (re-find #"^\d{9}$" pid)))

(defn valid-passport-part-two?
  "Return whether the provided passport is valid per the advanced rules in part two"
  [passport]
  (when (valid-passport? passport)
    (->> (for [[field valid?] {"byr" valid-birth-year?
                               "eyr" valid-expiration-year?
                               "iyr" valid-issue-year?
                               "hgt" valid-height?
                               "hcl" valid-hair-color?
                               "ecl" valid-eye-color?
                               "pid" valid-pid?}]
           (valid? (get passport field)))
         (every? true?))))


(comment
  ;; Problem 1
  (->> input
       (filter valid-passport?)
       (count))
  ;; Problem 2
  (->> input
       (filter valid-passport-part-two?)
       (count))
  (valid-passport? (first (lines->passports input))))
