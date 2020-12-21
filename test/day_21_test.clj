(ns day-21-test
  (:require [day-21 :as sut]
            [clojure.test :refer [deftest testing is]]
            [cuerdas.core :as str]))


(def foods
  (->> (slurp "test/fixtures/day21_test.txt")
       (str/lines)
       (mapv sut/line->food)))

(deftest line->food-test
  (is (= {:ingredients #{"mxmxvkd" "kfcds" "sqjhc" "nhms"}
          :allergens   #{"dairy" "fish"}}
         (sut/line->food "mxmxvkd kfcds sqjhc nhms (contains dairy, fish)"))))

(deftest top-candidates-test
  (is (= #{"a" "c"}
         (sut/top-candidates {"a" 2
                              "b" 1
                              "c" 2}))))

(deftest deduce-ingredient-test
  (is (= "fvjkl"
         (sut/deduce-ingredient
           "soy"
           (sut/ingredients->allergen-frequencies foods)
           (sut/allergens->ingredient-frequencies foods)))))

(deftest ingredients->allergens-test
  (is (= {"fvjkl" "soy" "sqjhc" "fish" "mxmxvkd" "dairy"}
         (sut/ingredients->allergens foods))))

(deftest solve-part-one-test
  (is (= 5 (sut/solve-part-one foods))))

(deftest solve-part-two-test
  (is (= "mxmxvkd,sqjhc,fvjkl"
         (sut/solve-part-two foods))))

(comment
  (sut/ingredients->allergens foods)
  (clojure.pprint/pprint
    (sut/allergens->ingredient-frequencies foods))
  (clojure.pprint/pprint
    (sut/ingredients->allergen-frequencies foods)))
