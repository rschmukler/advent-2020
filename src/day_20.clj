(ns day-20
  (:require [cuerdas.core :as str]
            [clojure.java.io :as io]
            [clojure.set :as set]))

(defn lines->tile
  "Convert a sequence of lines into a map of `[y x]` and value of `0` or `1`."
  [lines]
  (->> (for [[line-ix line] (map-indexed vector  lines)]
         (for [[char-ix char] (map-indexed vector line)]
           [[line-ix char-ix] (case char
                                \#     1
                                \.     0
                                \space nil)]))
       (apply concat)
       (filter (comp some? second))
       (into {})))

(defn str->tiles
  "Convert puzzle input into a tiles map"
  [s]
  (->> (str/split s "\n\n")
       (map (fn [bundle]
              (let [lines (str/lines bundle)
                    id    (->> lines
                               (first)
                               (re-find #"\d+")
                               (read-string))]
                [id (lines->tile (rest lines))])))
       (into {})))

(def tiles
  (-> (io/resource "day20_input.txt")
      (io/reader)
      (slurp)
      (str->tiles)))

(defn rotate-tile
  "Rotate the provided tile input 90 deg to the right"
  [tile]
  (let [max-x (apply max (->> tile
                              (keys)
                              (map second)))]
    (->> tile
         (map (fn [[[y x] val]]
                [[x (- max-x y)] val]))
         (into {}))))

(defn rotations
  "Return all rotations for the provided tile"
  [tile]
  (take 4 (iterate rotate-tile tile)))

(defn flips
  "Return the provided tile as itself, flipped across x, across y, and across x and y"
  [tile]
  (let [max-x (apply max (->> tile keys (map second)))
        max-y (apply max (->> tile keys (map first)))]
    (for [flip-x? [false true]
          flip-y? [false true]]
      (->> tile
           (map (fn [[[y x] val]]
                  [[(if flip-y?
                      (- max-y y)
                      y)
                    (if flip-x?
                      (- max-x x)
                      x)] val]))
           (into {})))))

(defn permutations
  "Return all possible permutations for the provided tile"
  [tile]
  (->> (for [tile (rotations tile)]
         (for [tile (flips tile)]
           tile))
       (apply concat)
       distinct))

(defn build-index
  "Build an index of tiles by indexing side using their bitmask.

  By building a map of constraints to sets of tiles, and taking the intersection of these sets,
  we can consider only tiles that will match all required constraints."
  [tiles]
  (->> (for [[tile-id tile] tiles]
         (for [perm (permutations tile)]
           {:id     tile-id
            :value  perm
            :left   (Integer/parseInt
                      (apply str (for [y (range 10)]
                                   (get perm [y 0]))) 2)
            :right  (Integer/parseInt
                      (apply str (for [y (range 10)]
                                   (get perm [y 9]))) 2)
            :top    (Integer/parseInt
                      (apply str (for [x (range 10)]
                                   (get perm [0 x]))) 2)
            :bottom (Integer/parseInt
                      (apply str (for [x (range 10)]
                                   (get perm [9 x]))) 2)}))
       (apply concat)
       (reduce
         (fn [acc tile]
           (-> acc
               (update :all (fnil conj #{}) tile)
               (update-in [:left (:left tile)] (fnil conj #{}) tile)
               (update-in [:right (:right tile)] (fnil conj #{}) tile)
               (update-in [:top (:top tile)] (fnil conj #{}) tile)
               (update-in [:bottom (:bottom tile)] (fnil conj #{}) tile)))
         {})))

(defn constraints
  "Return constraints for the provided coordinate"
  [{:keys [tiles]} [y x]]
  (let [result
        (->> {:left   (some-> (tiles [y (dec x)])
                              :right)
              :right  (some-> (tiles [y (inc x)])
                              :left)
              :top    (some-> (tiles [(dec y) x])
                              :bottom)
              :bottom (some-> (tiles [(inc y) x])
                              :top)}
             (filter val)
             (into {}))]
    (when (seq result)
      result)))

(defn valid-candidates
  "Return all candidates that could be used to fill the given
  `loc` using the provided `state`"
  [{:keys [index used-ids] :as state} loc]
  (->> (if-some [cs (constraints state loc)]
         (reduce set/intersection (map #(get-in index %) cs))
         (:all index))
       (remove (comp used-ids :id))))

(defn arrange-tiles
  "Arrange the provided tiles. Optionally takes an existing board state."
  ([tiles]
   (let [size (int (Math/sqrt (count tiles)))]
     (arrange-tiles {:size     (int (Math/sqrt (count tiles)))
                     :index    (build-index tiles)
                     :used-ids #{}
                     :tiles    {}}
                    (for [y (range size)
                          x (range size)]
                      [y x]))))
  ([state coords]
   (if (empty? coords)
     state
     (let [loc (first coords)]
       (->> (for [candidate (valid-candidates state loc)
                  :let      [new-state (-> state
                                           (assoc-in [:tiles loc] candidate)
                                           (update :used-ids conj (:id candidate)))]]
              (arrange-tiles new-state (rest coords)))
            (filter some?)
            first)))))

(defn solve-part-one
  "Solve the answer for part one"
  [tiles]
  (let [solution (arrange-tiles tiles)]
    (->> (for [y [0 (dec (:size solution))]
               x [0 (dec (:size solution))]]
           (:id (get-in solution [:tiles [y x]])))
         (apply *))))

;; Part two stuff
(def seamonster-mask
  "The mask for the sea monster"
  (-> (io/resource "day20_seamonster.txt")
      (io/reader)
      (line-seq)
      (lines->tile)))

(defn assemble-image
  "Return the assembled image using the output from `arrange-tiles`."
  [{:keys [size tiles]}]
  (->> (for [tile-y (range size)
             tile-x (range size)
             :let   [tile (get-in tiles [[tile-y tile-x] :value])]]
         (for [y (range 1 9)
               x (range 1 9)]
           [[(+ (dec y) (* 8 tile-y))
             (+ (dec x) (* 8 tile-x))]
            (get tile [y x])]))
       (apply concat)
       (into {})))

(defn sea-monster-at-loc?
  "Return whether the is a seamonster at the provided coordinate"
  [image [y x]]
  (every? (fn [[mask-y mask-x]]
            (= 1 (get image [(+ y mask-y) (+ x mask-x)])))
          (keys seamonster-mask)))

(defn find-sea-monsters
  "Find all sea monsters in the image"
  [image]
  (let [size   (int (Math/sqrt (count image)))
        mask-x (apply max (map (comp second key) seamonster-mask))
        mask-y (apply max (map (comp first key) seamonster-mask))]
    (for [y     (range (- size mask-y))
          x     (range (- size mask-x))
          :when (sea-monster-at-loc? image [y x])]
      [y x])))

(defn solve-part-two
  "Return the number of rough water tiles that aren't occupied by sea monsters!"
  [tiles]
  (let [[image monster-count] (->> tiles
                                   (arrange-tiles)
                                   (assemble-image)
                                   (permutations)
                                   (some
                                     (fn [image]
                                       (let [monster-count (count (find-sea-monsters image))]
                                         (when (pos? monster-count)
                                           [image monster-count])))))
        rough-count           (->> image
                                   (filter (comp #{1} val))
                                   count)]
    (- rough-count (* monster-count (count seamonster-mask)))))

(comment
  (time
    (solve-part-one tiles))
  (time
    (solve-part-two tiles))

  )
