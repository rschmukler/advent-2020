(ns day-18-test
  (:require [day-18 :as sut]
            [clojure.test :refer [deftest is]]))


(deftest parse-expr-test
  (is (= '(2 * 3 + (4 * 5)) (sut/parse-expr "2 * 3 + (4 * 5)"))))

(deftest eval-expr-test
  (is (= 51 (sut/eval-str "1 + (2 * 3) + (4 * (5 + 6))")))
  (is (= 26 (sut/eval-str "2 * 3 + (4 * 5)")))
  (is (= 437 (sut/eval-str "5 + (8 * 3 + 9 + 3 * 4 * 3)")))
  (is (= 12240 (sut/eval-str "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))")))
  (is (= 13632 (sut/eval-str "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"))))

(deftest eval-expr-advanced-test
  (is (= 51 (sut/eval-advanced-str "1 + (2 * 3) + (4 * (5 + 6))")))
  (is (= 46 (sut/eval-advanced-str "2 * 3 + (4 * 5)")))
  (is (= 1445 (sut/eval-advanced-str "5 + (8 * 3 + 9 + 3 * 4 * 3)")))
  (is (= 669060 (sut/eval-advanced-str "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))")))
  (is (= 23340 (sut/eval-advanced-str "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"))))
