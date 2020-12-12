(ns day-12-test
  (:require [day-12 :as sut]
            [clojure.test :refer [deftest testing is]]
            [clojure.string :as str]))

(def input
  (->> (slurp "test/fixtures/day12_test.txt")
       (str/split-lines)
       (mapv sut/line->instruction)))


(deftest line->instruction-test
  (is (= [:forward 10] (first input))))

(deftest apply-instruction-test
  (is (= {:north 0 :east 10 :direction :east}
         (sut/apply-instruction {:north 0 :east 0 :direction :east}
                                [:forward 10]))))

(deftest solve-part-one-test
  (is (= 25 (sut/solve-using sut/apply-instruction-part-one input))))

(deftest rotate-waypoint-test
  (testing "rotate R90"
    (let [ship (sut/rotate-waypoint {:waypoint-east 10 :waypoint-north 4} 90)]
      (is (= 4 (:waypoint-east ship)))
      (is (= -10 (:waypoint-north ship)))))

  (testing "rotate L90"
    (let [ship (sut/rotate-waypoint {:waypoint-east 10 :waypoint-north 4} (- 360 90))]
      (is (= -4 (:waypoint-east ship)))
      (is (= 10 (:waypoint-north ship))))))

(deftest move-toward-waypoint-test
  (let [ship (sut/move-toward-waypoint {:east          0  :north          0
                                        :waypoint-east 10 :waypoint-north 1}
                                       10)]
    (is (= 100 (:east ship)))
    (is (= 10 (:north ship)))))

(deftest solve-part-two-test
  (is (= 286 (sut/solve-using sut/apply-instruction-part-two input))))
