(ns day-19-test
  (:require [day-19 :as sut]
            [clojure.test :refer [deftest testing is]]
            [cuerdas.core :as str]))

(def test-rules
  (-> "0: 4 1 5
       1: 2 3 | 3 2
       2: 4 4 | 5 5
       3: 4 5 | 5 4
       4: \"a\"
       5: \"b\""
      (str/lines)
      (->> (map str/trim)
           (map sut/line->rule)
           (into {}))))

(def part-two-sample
  (sut/process-file "test/fixtures/day19_test.txt"))

(def messages
  (:messages part-two-sample))

(def rules
  (:rules part-two-sample))


(deftest line->rule-test
  (testing "literal rule"
    (is (= [3 [:literal \b]]
           (sut/line->rule "3: \"b\""))))

  (testing "or rule"
    (is (= [1 [:or [:seq 2 3] [:seq 3 2]]]
           (sut/line->rule "1: 2 3 | 3 2")))

    (is (= [1 [:or [:seq 116] [:seq 32]]]
           (sut/line->rule "1: 116 | 32"))))

  (testing "seq rule"
    (is (= [0 [:seq 4 1 5]]
           (sut/line->rule "0: 4 1 5")))))


(deftest count-matches-test
  (testing "non-recursive rules"
    (is (= 2 (sut/count-matches
               test-rules
               ["ababbb"
                "bababa"
                "abbbab"
                "aaabbb"
                "aaaabbb"]))))

  (let [{:keys [messages rules]} (sut/process-file "test/fixtures/day19_test.txt")]
    (testing "non-recursive rules"
      (is (= 3 (sut/count-matches rules messages))))

    (testing "recursive-rules"
      (is (= 12
             (sut/count-matches
               (->> ["8: 42 | 42 8"
                     "11: 42 31 | 42 11 31"]
                    (map sut/line->rule)
                    (into rules))
               messages))))))
