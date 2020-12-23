(ns day-23)

(def input
  "Our puzzle input"
  [4 8 7 9 1 2 3 6 5])


(defn ->game
  "Initialize a game state with the provided cups"
  [cups]
  {:cup       (first cups)
   :cup->next (let [cup->next (->> cups
                                   (partition 2 1)
                                   (map vec)
                                   (into {}))]
                (assoc cup->next (last cups) (first cups)))
   :max-cup   (apply max cups)})

(defn cup-seq
  "Return a sequence of cups from the game state"
  ([game] (cup-seq true game))
  ([infinite? game]
   (letfn [(->seq [{:keys [cup->next cup]} seen]
             (when-not (and cup (seen cup))
               (lazy-seq
                 (cons cup (->seq (assoc game :cup (cup->next cup)) (if-not infinite?
                                                                      (conj seen cup)
                                                                      seen))))))]
     (->seq game #{}))))

(defn turn
  [{:keys [cup max-cup cup->next] :as game}]
  (let [picked-up-cups  (->> (cup-seq game)
                             (drop 1)
                             (take 3))
        next-cup        (nth (cup-seq game) 4)
        picked-up-set   (set picked-up-cups)
        destination-cup (loop [tgt (dec cup)]
                          (cond
                            (zero? tgt)                         (recur max-cup)
                            (not (contains? picked-up-set tgt)) tgt
                            :else                               (recur (dec tgt))))
        next-dst-cup    (cup->next destination-cup)]
    (-> game
        (assoc :cup next-cup)
        (assoc-in [:cup->next cup] next-cup)
        (assoc-in [:cup->next destination-cup] (first picked-up-cups))
        (assoc-in [:cup->next (last picked-up-cups)] next-dst-cup))))

(defn play-n-turns
  "Return the game state after playing `n` turns using the initial `cups`"
  [n cups]
  (->> (->game cups)
       (iterate turn)
       (drop n)
       (first)))

(defn cups-after-one
  "Return `n` cups after one with the provided end state"
  [n game-state]
  (->> (assoc game-state :cup 1)
       (cup-seq)
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
  (solve-part-two input))
