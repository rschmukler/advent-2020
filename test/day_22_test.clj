(ns day-22-test
  (:require [day-22 :as sut]
            [clojure.test :refer [deftest testing is]]))


(def player-decks
  (-> (slurp "test/fixtures/day22_test.txt")
      (sut/input->player-decks)))

(deftest input->player-decks-test
  (is (= {1 [9 2 6 3 1]
          2 [5 8 4 7 10]}
         player-decks)))

(deftest play-game-test
  (is (= {:winner 2
          :deck   [3 2 10 6 8 5 9 4 7 1]}
         (sut/play-game player-decks))))

(deftest play-recursive-combat-test
  (is (= 1 (:winner
            (sut/play-recursive-combat {1 [43 19]
                                        2 [2 29 14]}))))
  (is (= {:winner 2
          :deck   [7 5 6 2 4 1 10 8 9 3]}
         (sut/play-recursive-combat player-decks))))

(deftest compute-score-test
  (is (= 306
         (-> player-decks sut/play-game sut/compute-score)))

  (is (= 291
         (-> player-decks sut/play-recursive-combat sut/compute-score))))
