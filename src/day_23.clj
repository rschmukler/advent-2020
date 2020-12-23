(ns day-23)

(def input
  "Our puzzle input"
  [4 8 7 9 1 2 3 6 5])


(defn chain->value-seq
  "Convert a chain node into a sequence of values"
  [chain]
  (letfn [(->seq [chain seen]
            (when-not (seen chain)
              (lazy-seq
                (cons (:value @chain) (->seq (:next @chain) (conj seen chain))))))]
    (->seq chain #{})))


(defn ->game
  "Initialize a game state with the provided cups"
  [cups]
  (let [vs    (->> cups
                   (mapv #(volatile! {:value %})))
        vs-ix (->> vs
                   (map (juxt (comp :value deref) identity))
                   (into {}))]
    (doseq [[a next] (partition 2 1 vs)]
      (vswap! a assoc :next next))

    (vswap! (last vs) assoc :next (first vs))
    {:ptr     (vs-ix (first cups))
     :index   vs-ix
     :max-cup (apply max cups)}))

(defn turn!
  [{:keys [ptr max-cup index] :as game}]
  (let [cup             (:value @ptr)
        picked-up-cups  (->> (iterate (comp :next deref) ptr)
                             (drop 1)
                             (take 3))
        other-cups      (nth (iterate (comp :next deref) ptr) 4)
        picked-up-set   (set (map (comp :value deref) picked-up-cups))
        destination-cup (loop [tgt (dec cup)]
                          (cond
                            (zero? tgt)                         (recur max-cup)
                            (not (contains? picked-up-set tgt)) tgt
                            :else                               (recur (dec tgt))))

        dest-ptr (get index destination-cup)
        next-cup (:next @dest-ptr)]
    (vswap! ptr assoc :next other-cups)
    (vswap! dest-ptr assoc :next (first picked-up-cups))
    (vswap! (last picked-up-cups) assoc :next next-cup)
    (assoc game :ptr (:next @ptr))))

(defn play-n-turns
  "Return the game state after playing `n` turns using the initial `cups`"
  [n cups]
  (->> (->game cups)
       (iterate turn!)
       (drop n)
       (first)))

(defn cups-after-one
  "Return `n` cups after one with the provided end state"
  [n game-state]
  (->> (get (:index game-state) 1)
       (chain->value-seq)
       (drop 1)
       (take n)))


(defn solve-part-one
  ([cups] (solve-part-one 100 cups))
  ([moves cups]
   (->> cups
        (play-n-turns moves)
        (cups-after-one (dec (count cups)))
        (apply str))))

(defn solve-part-two
  ([cups] (solve-part-two 10000000 cups))
  ([moves cups]
   (let [all-cups (concat cups (range (inc (count cups)) 1000001))]
     (->> all-cups
          (play-n-turns moves)
          (cups-after-one 2)
          (apply *)))))

(comment
  (solve-part-two))
