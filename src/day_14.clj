(ns day-14
  "Please be easy so I can go to sleep at a reasonable hour"
  (:require [clojure.java.io :as io]
            [clojure.core.match :refer [match]]
            [cuerdas.core :as str]))

(defn line->instruction
  "Convert a line of text into an instruction"
  [line]
  (if (re-find #"mask" line)
    [:mask (-> line
               (str/split #"=")
               (second)
               (str/trim))]
    (into [:mem] (map read-string (re-seq #"\d+" line)))))

(def input
  "Input for day 14"
  (->> (io/resource "day14_input.txt")
       (io/reader)
       (line-seq)
       (mapv line->instruction)))

(defn long->binary-str
  "Convert a long into a string of binary"
  [l]
  (str/pad
    (Integer/toBinaryString l)
    {:length  36
     :padding "0"}))

(defn masked-value
  "Return the value of `value` after applying the provided `mask`"
  [mask value]
  (->> (map
         (fn [mask-bit value-bit]
           (match [mask-bit value-bit]
             [\X v] v
             [\1 _] \1
             [\0 _] \0))
         mask
         (long->binary-str value))
       (apply str)
       (#(Long/parseLong % 2))))

(defn permute-floating-mask
  "Convert a mask with floating strings into a sequence of all of the various
  permutations of floating addresses being resolved to either zero or one."
  [floating-mask]
  (if-not (re-find #"X" floating-mask)
    [floating-mask]
    (lazy-cat
      (permute-floating-mask (str/replace-first floating-mask "X" "0"))
      (permute-floating-mask (str/replace-first floating-mask "X" "1")))))

(defn masked-addresses
  "Return a sequence of addresses (longs) that should be written to
  using the provided input mask and address value"
  [mask addr]
  (let [floating-mask (->> (map
                             (fn [mask-bit value-bit]
                               (match [mask-bit value-bit]
                                 [\X _] \X
                                 [\1 _] \1
                                 [\0 _] value-bit))
                             mask
                             (long->binary-str addr))
                           (apply str))]
    (->> floating-mask
         (permute-floating-mask)
         (map #(Long/parseLong % 2)))))


(defn v1-decoder
  "The old busted v1 decoder"
  [state instruction]
  (match instruction
    [:mask mask] (assoc state :mask mask)
    [:mem addr val] (assoc-in state [:memory addr] (masked-value (:mask state) val))))


(defn v2-decoder
  "The new shiny Cyberpunk 2077 decoder"
  [state instruction]
  (match instruction
    [:mask mask] (assoc state :mask mask)
    [:mem addr mem-val] (->> (masked-addresses (:mask state) addr)
                             (reduce #(assoc-in %1 [:memory %2] mem-val) state))))

(defn run-program
  "Run the provided instructions using the provided decoder function and instructions"
  [decoder instructions]
  (reduce
    decoder
    {:memory {}
     :mask   nil}
    instructions))

(defn solve-using
  "Sums the memory values in the computer after running the provided `decoder`
  over instructions."
  [decoder instructions]
  (->> instructions
       (run-program decoder)
       :memory
       (vals)
       (reduce +)))

(comment
  (solve-using v1-decoder input)
  (solve-using v2-decoder input))
