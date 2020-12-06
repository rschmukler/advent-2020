(ns day-6-test
  (:require [day-6 :as sut]
            [clojure.test :refer [deftest testing is]]))

(def input
  (->> (slurp "test/fixtures/day6_test.txt")))


(deftest input->group-answers-test
  (is (= '((#{\a \b \c})
           (#{\a} #{\b} #{\c})
           (#{\a \b} #{\a \c})
           (#{\a} #{\a} #{\a} #{\a})
           (#{\b}))
         (sut/input->group-answers input))))

(deftest solve-using-test
  (testing "with any yes (part 1)"
    (is (= 11 (sut/solve-using sut/answers-with-any-yes input))))
  (testing "with all yes (part 2)"
    (is (= 6 (sut/solve-using sut/answers-with-all-yes input)))))
