(ns day-15)

(def input
  "Input for day 15"
  [18 11 9 0 5 1])


(defn speak-number
  "Iterative function that updates the state with the latest number."
  [{:keys [starting-numbers history last-number turn]}]
  (let [this-turn   (inc turn)
        history-val (get history last-number)
        next-number (cond
                      (seq starting-numbers)    (first starting-numbers)
                      (= 2 (count history-val)) (apply - history-val)
                      :else                     0)]
    {:last-number      next-number
     :turn             this-turn
     :history          (update history next-number (comp #(take 2 %) conj) this-turn)
     :starting-numbers (rest starting-numbers)}))

(defn play-game
  "Return an infinite sequence of numbers spoken for the game"
  [input]
  (->> (iterate speak-number {:starting-numbers input
                              :history          {}
                              :last-number      nil
                              :turn             0})
       (map :last-number)
       (rest)))

(comment
  ;; Part one
  (nth (play-game input) 2019)

  ;; Part two
  (nth (play-game input) (dec 30000000)))
