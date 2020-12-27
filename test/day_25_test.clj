(ns day-25-test
  (:require [day-25 :as sut]
            [clojure.test :refer [deftest testing is]]))


(deftest transform-number-test
  (is (= 5764801 (sut/transform-number 7 8))))

(deftest crack-loop-size
  (is (= 8 (sut/crack-loop-size 5764801)))
  (is (= 11 (sut/crack-loop-size 17807724))))

(deftest crack-encyption-key
  (is (= 14897079 (sut/crack-encyption-key 5764801 17807724))))
