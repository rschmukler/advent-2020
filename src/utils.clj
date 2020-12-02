(ns utils
  "Shared utility functions that might come in handy
  again some day.")

(defn xor
  "Variadic exclusive or"
  [& args]
  (loop [result     false
         [x :as xs] args]
    (cond
      (empty? xs)    result
      (and x result) false
      :else          (recur (or x result) (rest xs)))))
