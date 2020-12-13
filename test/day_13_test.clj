(ns day-13-test
  (:require [day-13 :as sut]
            [clojure.test :refer [deftest testing is]]
            [clojure.string :as str]))

(def input
  (->> (slurp "test/fixtures/day13_test.txt")
       (str/split-lines)
       (sut/read-schedule)))

(deftest read-schedule-test
  (is (= 939 (:timestamp input)))
  (is (= [{:id 7 :ix 0}
          {:id 13 :ix 1}
          {:id 59 :ix 4}
          {:id 31 :ix 6}
          {:id 19 :ix 7}] (:buses input))))

(deftest solve-part-one-test
  (is (= 295 (sut/solve-part-one input))))

(deftest valid-departure-test?
  (is (sut/valid-departure? {:id 7 :ix 0} 1068774))
  (is (sut/valid-departure? {:id 13 :ix 1} 1068781))
  (is (not (sut/valid-departure? {:id 41 :ix 13} 0))))

(deftest solve-part-two-test
  (is (= 3417 (sut/solve-part-two {:buses [{:id 17 :ix 0}
                                           {:id 13 :ix 2}
                                           {:id 19 :ix 3}]})))

  (is (= 754018 (sut/solve-part-two
                  {:buses [{:id 67 :ix 0}
                           {:id 7 :ix 1}
                           {:id 59 :ix 2}
                           {:id 61 :ix 3}]})))

  (is (= 779210 (sut/solve-part-two
                  {:buses [{:id 67 :ix 0}
                           {:id 7 :ix 2}
                           {:id 59 :ix 3}
                           {:id 61 :ix 4}]})))

  (is (= 779210 (sut/solve-part-two
                  {:buses [{:id 67 :ix 0}
                           {:id 7 :ix 2}
                           {:id 59 :ix 3}
                           {:id 61 :ix 4}]})))

  (is (= 1261476 (sut/solve-part-two
                   {:buses [{:id 67 :ix 0}
                            {:id 7 :ix 1}
                            {:id 59 :ix 3}
                            {:id 61 :ix 4}]})))

  (is (= 1202161486 (sut/solve-part-two
                      {:buses [{:id 1789 :ix 0}
                               {:id 37 :ix 1}
                               {:id 47 :ix 2}
                               {:id 1889 :ix 3}]}))))

(comment
  (every?
    #(sut/valid-departure? % 1202161486)
    [{:id 1789 :ix 0}
     {:id 37 :ix 1}
     {:id 47 :ix 2}
     {:id 1889 :ix 3}]))
