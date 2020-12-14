(ns day-14-test
  (:require [day-14 :as sut]
            [clojure.test :refer [deftest testing is]]
            [clojure.string :as str]))

(def input
  (->> (slurp "test/fixtures/day14_test.txt")
       (str/split-lines)
       (mapv sut/line->instruction)))

(deftest line->instruction-test
  (is (= [:mask "XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X"]
         (sut/line->instruction "mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X")))

  (is (= [:mem 8 11]
         (sut/line->instruction "mem[8] = 11"))))

(deftest masked-value-test
  (is (= 73 (sut/masked-value "XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X" 11))))

(deftest masked-addresses-test
  (is (= [26 27 58 59]
         (sut/masked-addresses "000000000000000000000000000000X1001X" 42))))

(deftest solve-using-test
  (is (= 165 (sut/solve-using sut/v1-decoder input)))
  (is (= 208 (sut/solve-using
               sut/v2-decoder
               [[:mask "000000000000000000000000000000X1001X"]
                [:mem 42 100]
                [:mask "00000000000000000000000000000000X0XX"]
                [:mem 26 1]]))))
