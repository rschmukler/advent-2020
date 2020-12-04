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


(deftest missing-fields-test
  (is (= #{} (sut/missing-fields (first input)))))

(deftest valid-passport?-test
  (is (= 2 (count (->> input
                       (filter sut/valid-passport?))))))

(deftest field-validation-tests
  (is (sut/valid? "byr" "2002"))
  (is (not (sut/valid? "byr" "2003")))

  (is (sut/valid? "hgt" "60in"))
  (is (not (sut/valid? "hgt" "190in")))

  (is (sut/valid? "hcl" "#123abc"))
  (is (not (sut/valid? "hcl" "#123abz")))

  (is (sut/valid? "ecl" "brn"))
  (is (not (sut/valid? "ecl" "wat")))

  (is (sut/valid? "pid" "000000001"))
  (is (not (sut/valid? "pid" "0123456789"))))

(deftest part-two-invalid-test
  (doseq [passport invalid-passports]
    (is (not (sut/valid-passport-part-two? passport)))))
