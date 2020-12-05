(ns day-5-test
  (:require [day-5 :as sut]
            [clojure.test :refer [deftest is]]))

(deftest line->seat-test
  (is (= {:row 70 :column 7 :id 567}
         (sut/line->seat "BFFFBBFRRR")))
  (is (= {:row 14 :column 7 :id 119}
         (sut/line->seat "FFFBBBFRRR")))
  (is (= {:row 102 :column 4 :id 820}
         (sut/line->seat "BBFFBBFRLL"))))
