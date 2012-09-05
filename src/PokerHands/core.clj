(ns PokerHands.core
	(:use [clojure.string :only [split]])
  (:gen-class))

(defrecord Card [rank suit])
(defrecord Score [type])

(defn parse-suit [r]
		(condp = r
			\H :Hearts
			\S :Spades
			\C :Clubs
			\D :Diamonds
			)) 

(defn parse-rank [r]
		(condp = r
			\A 14
			\K 13
			\Q 12
			\J 11
			\T 10
			\9 9
			\8 8
			\7 7
			\6 6
			\5 5
			\4 4
			\3 3
			\2 2)) 

(defn c [str] (->Card (parse-rank (first str)) (parse-suit (last str))))

(defn hand [str]
	(map c (split str #" ")))

(defn ranks [hand]
	(sort > (map :rank hand)))

(defn consec-pair? [rank1 rank2] (= 1 (- rank1 rank2)))

(defn consec? [ranks]
	(if (> 2 (count ranks)) 
		true
		(and (consec-pair? (first ranks) (second ranks))
	         (consec? (rest ranks)))))

(defn is-straight? [hand] (consec? (ranks hand)))

(defn score [hand]
	(cond
		(is-straight? hand) (->Score :Straight)))
