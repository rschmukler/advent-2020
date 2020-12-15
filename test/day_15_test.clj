(ns day-15-test
  (:require [day-15 :as sut]
            [clojure.test :refer [deftest testing is]]))

(def sample-input
  [0 3 6])

(deftest play-game-test
  (testing "starts with starting numbers"
    (is (= [0 3 6] (take 3 (sut/play-game sample-input)))))

  (is (= [0 3 6 0 3 3 1 0 4 0]
         (take 10 (sut/play-game sample-input))))

  (is (= 436 (nth (sut/play-game sample-input) 2019)))
  (is (= 1 (nth (sut/play-game [1 3 2]) 2019))))
