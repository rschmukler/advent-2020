(ns day-2-test
  (:require [day-2 :as sut]
            [clojure.test :refer [deftest is]]
            [clojure.string :as str]))


(def raw-input
  "1-3 a: abcde\n1-3 b: cdefg\n2-9 c: ccccccccc")

(deftest line->policy-check-test
  (is (= {:min 1 :max 3 :letter \a :password "abcde"}
         (-> (str/split-lines raw-input)
             first
             (sut/line->policy-check)))))

(deftest valid-part-one-policy?-test
  (is (sut/valid-part-one-policy? {:min 1 :max 3 :letter \a :password "abcde"}))
  (is (not (sut/valid-part-one-policy? {:min 1 :max 3 :letter \b :password "cdefg"}))))

(deftest valid-part-two-policy?-test
  (is (sut/valid-part-two-policy?
        {:min 1 :max 3 :letter \a :password "abcde"}))
  (is (not (sut/valid-part-two-policy?
             {:min 1 :max 3 :letter \b :password "cdefg"}))))
