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

(defn gcd
  "Greatest common denominator for the provided nums"
  [a b & args]
  (let [gcd (fn gcd [a b]
              (if (zero? b)
                a
                (recur b (mod a b))))]
    (reduce gcd (gcd a b) args)))

(defn least-common-multiple
  "Returns the least common multiple for the provided nums"
  [a b & args]
  (let [lcm (fn lcm [a b]
              (/ (* a b) (gcd a b)))]
    (reduce lcm (lcm a b) args)))

(defn find-first
  "Return the first item in collection that returns truthy for `pred`"
  [pred coll]
  (some #(and (pred %) %) coll))
