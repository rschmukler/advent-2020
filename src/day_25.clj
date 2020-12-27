(ns day-25)

(set! *unchecked-math* true)

(def public-keys
  [8184785
   5293040])

(defn transform-number
  "Apply the number transformation to `n` using the provided loop size"
  [^long n ^long loop-size]
  (loop [result    1
         loop-size loop-size]
    (if (zero? loop-size)
      result
      (recur (rem (* result n) 20201227) (dec loop-size)))))


(defn crack-loop-size
  "Return the secret loop size for the provided `public-key`"
  [^long public-key]
  (loop [n         1
         loop-size 0]
    (if (= public-key n)
      loop-size
      (recur (rem (* n 7) 20201227) (inc loop-size)))))


(defn crack-encyption-key
  "Crack the encyption key using the two public keys"
  [key-a key-b]
  (let [loop-size (crack-loop-size key-a)]
    (transform-number key-b loop-size)))

(comment
  ;; Solve part one
  (apply crack-encyption-key public-keys))
