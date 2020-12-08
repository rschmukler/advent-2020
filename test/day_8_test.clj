(ns day-8-test
  (:require [day-8 :as sut]
            [clojure.test :refer [deftest testing is]]
            [clojure.string :as str]))

(def input
  (->> (slurp "test/fixtures/day8_test.txt")
       (str/split-lines)
       (mapv sut/line->instruction)))

(deftest run-non-terminating-program-test
  (is (= [5 false] (sut/run-program input))))

(deftest fix-non-terminating-program-test
  (is (= 8 (sut/fix-non-terminating-program input {:nop :jmp
                                                   :jmp :nop}))))
