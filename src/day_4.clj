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
        found    (set (keys passport))]
    (set/difference expected found)))

(defn valid-passport?
  "Return whether the provided passport is valid"
  [passport]
  (case (missing-fields passport)
    #{"cid"} true
    #{}      true
    false))

(defmulti valid?
  "Return whether the provided field is valid"
  (fn [field _]
    field))

(defmethod valid? :default
  [_ _]
  true)

(defmethod valid? "byr"
  [_ value]
  (<= 1920 (read-string value) 2002))

(defmethod valid? "eyr"
  [_ value]
  (<= 2020 (read-string value) 2030))

(defmethod valid? "iyr"
  [_ value]
  (<= 2010 (read-string value) 2020))

(defmethod valid? "hgt"
  [_ height]
  (when-some [[_ num unit] (re-find #"^(\d+)(cm|in)$" height)]
    (let [num (read-string num)]
      (case unit
        "cm" (<= 150 num 193)
        "in" (<= 59 num 76)
        false))))

(defmethod valid? "hcl"
  [_ hair-color]
  (some? (re-find #"^#([a-f0-9]{6})$" hair-color)))

(defmethod valid? "ecl"
  [_ eye-color]
  (contains? #{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"} eye-color))

(defmethod valid? "pid"
  [_ pid]
  (some? (re-find #"^\d{9}$" pid)))

(defn valid-passport-part-two?
  "Return whether the provided passport is valid per the advanced rules in part two"
  [passport]
  (reduce-kv
    #(and %1 (valid? %2 %3))
    (valid-passport? passport)
    passport))


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
