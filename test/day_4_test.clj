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
  (is (sut/valid-birth-year? "2002"))
  (is (not (sut/valid-birth-year? "2003")))

  (is (sut/valid-height? "60in"))
  (is (not (sut/valid-height? "190in")))

  (is (sut/valid-hair-color? "#123abc"))
  (is (not (sut/valid-hair-color? "#123abz")))

  (is (sut/valid-eye-color? "brn"))
  (is (not (sut/valid-eye-color? "wat")))

  (is (sut/valid-pid? "000000001"))
  (is (not (sut/valid-pid? "0123456789"))))

(deftest part-two-invalid-test
  (doseq [passport invalid-passports]
    (is (not (sut/valid-passport-part-two? passport)))))
