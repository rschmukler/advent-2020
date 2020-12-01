(ns day-1-test
  (:require [day-1 :as sut]
            [clojure.test :refer [deftest is]]))

(def sample-input
  [1721
   979
   366
   299
   675
   1456])

(deftest find-two-numbers-test
  (is (= #{1721 299}
         (sut/find-n-with-sum 2 2020 sample-input)))
  (is (= #{979 366 675}
         (sut/find-n-with-sum 3 2020 sample-input))))
