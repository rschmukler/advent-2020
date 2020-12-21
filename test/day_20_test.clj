(ns day-20-test
  (:require [day-20 :as sut]
            [clojure.test :refer [deftest testing is]]
            [cuerdas.core :as str]
            [clojure.set :as set]))


(def tiles
  (-> (slurp "test/fixtures/day20_test.txt")
      (sut/str->tiles)))

(def correct-tiles
  (-> (slurp "test/fixtures/day20_correct.txt")
      (sut/str->tiles)))

(def assembled-image
  (-> (slurp "test/fixtures/day20_assembled.txt")
      (str/lines)
      (sut/lines->tile)))

(def seamonster-image
  (-> (slurp "test/fixtures/day20_seamonster.txt")
      (str/lines)
      (sut/lines->tile)))


(deftest permutations-test
  (testing "we can generate the correct tile by permuting a raw tile"
    (doseq [[id correct-tile] correct-tiles]
      (is (some #{correct-tile} (sut/permutations (get tiles id)))))))

(deftest arrange-tiles-test
  (is (sut/arrange-tiles tiles))
  (is (sut/arrange-tiles correct-tiles))
  (is (= (sut/arrange-tiles tiles)
         (sut/arrange-tiles correct-tiles))))

(deftest solve-part-one-test
  (is (= 20899048083289 (sut/solve-part-one tiles))))

(deftest can-place-test?
  (is (sut/can-place?
        {[0 0] (get correct-tiles 1951)}
        [0 1] (get correct-tiles 2311))))


(deftest assemble-image-test
  (let [assembled (-> tiles
                      (sut/arrange-tiles)
                      (sut/assemble-image))]
    (is (= (count assembled-image) (count assembled)))
    (is (= assembled-image assembled))))

(deftest find-sea-monsters-test
  (is (= 2 (count (sut/find-sea-monsters seamonster-image)))))

(deftest solve-part-two-test
  (is (= 273 (sut/solve-part-two tiles))))
