(ns day-22
  (:require [cuerdas.core :as str]
            [data.deque :as dq]
            [clojure.java.io :as io]))

(defn input->player-decks
  "Convert a input string into map of player decks."
  [input]
  (->> (str/split input "\n\n")
       (map
         (comp
           (fn [ [header & cards]]
             [(read-string (re-find #"\d+" header))
              (mapv read-string cards)])
           str/lines))
       (into {})))

(def player-decks
  (-> (io/resource "day22_input.txt")
      (io/reader)
      slurp
      (input->player-decks)))

(defn win-round-and-take-card
  "Helper function to add the provided `card` to the end of the `deck` as well
  as pop the first item and add it to the back"
  [deck card]
  (-> deck
      (dq/remove-first)
      (dq/add-last (first deck))
      (dq/add-last card)))

(defn lose-round
  "Helper function to lose the round"
  [deck]
  (dq/remove-first deck))

(defn ->deck
  "Coerce the provided input into a deck, if it isn't one already"
  [coll]
  (if (or (vector? coll) (seq? coll))
    (apply dq/deque (reverse coll))
    coll))

(defn deck->vec
  "Helper to convert a deq to a vec"
  [deck]
  (vec (seq deck)))

(defn play-game
  "Play the simple (part one game)"
  [player-decks]
  (loop [[p1-card :as p1-deck] (->deck (player-decks 1))
         [p2-card :as p2-deck] (->deck (player-decks 2))]
    (cond
      (nil? p2-card)      {:winner 1
                           :deck   (vec (seq p1-deck))}
      (nil? p1-card)      {:winner 2
                           :deck   (vec (seq p2-deck))}
      (> p1-card p2-card) (recur (win-round-and-take-card p1-deck p2-card) (lose-round p2-deck))
      :else               (recur (lose-round p1-deck) (win-round-and-take-card p2-deck p1-card)))))


(defn play-recursive-combat
  "Lets get ready to rum... Lets get ready to rum... Lets get ready to rum..
  Lets get ready to rumbleeeeeeee."
  ([player-decks] (play-recursive-combat (->deck (player-decks 1))
                                         (->deck (player-decks 2))
                                         #{}))
  ([[p1-card :as p1-deck] [p2-card :as p2-deck] seen]
   (let [round-hash  [(deck->vec p1-deck)
                      (deck->vec p2-deck)]
         game-winner (cond
                       (contains? seen round-hash) 1
                       (nil? p1-card)              2
                       (nil? p2-card)              1
                       :else                       nil)
         new-seen    (conj seen round-hash)]
     (if (some? game-winner)
       {:winner game-winner
        :deck   (deck->vec
                  (if (= 1 game-winner)
                    p1-deck
                    p2-deck))}
       (let [round-winner (cond
                            ;; If we can recur...
                            (and (> (count p1-deck) p1-card)
                                 (> (count p2-deck) p2-card))
                            (:winner (play-recursive-combat
                                       (->deck (take p1-card (rest p1-deck)))
                                       (->deck (take p2-card (rest p2-deck)))
                                       new-seen))
                            (> p1-card p2-card) 1
                            (> p2-card p1-card) 2)
             p1-deck      (if (= 1 round-winner)
                            (win-round-and-take-card p1-deck p2-card)
                            (lose-round p1-deck))
             p2-deck      (if (= 2 round-winner)
                            (win-round-and-take-card p2-deck p1-card)
                            (lose-round p2-deck))]
         (recur p1-deck p2-deck new-seen))))))


(defn compute-score
  "Compute the score for the winning player"
  [win-state]
  (->> win-state
       :deck
       (reverse)
       (map-indexed #(* (inc %1) %2))
       (reduce +)))


(comment
  ;; Part one
  (-> player-decks
      play-game
      compute-score)

  ;; Part two
  (-> player-decks
      play-recursive-combat
      compute-score))
