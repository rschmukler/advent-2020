(ns day-4-test
  (:require [day-4 :as sut]
            [clojure.test :refer [deftest testing is]]
            [clojure.java.io :as io]))

(def input
  (->> (io/file "test/fixtures/day4_test.txt")
       (io/reader)
       (line-seq)
       (sut/lines->passports)))

(def invalid-passports
  (->> (io/file "test/fixtures/day4_invalid.txt")
       (io/reader)
       (line-seq)
       (sut/lines->passports)))

(deftest valid-passport?-test
  (is (= 2 (count (->> input
                       (filter sut/valid-passport-part-one?))))))

(deftest part-two-invalid-test
  (doseq [passport invalid-passports]
    (is (not (sut/valid-passport-part-two? passport)))))
