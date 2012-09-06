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

(defn c [str] 
	(->Card 
		(parse-rank (first str)) 
		(parse-suit (last str))))

(defn hand [str]
	(map c (split str #" ")))

(defn ranks [cards]
	(reverse (sort (map :rank cards))))

(defn consec-pair? [rank1 rank2] (= 1 (- rank1 rank2)))

(defn consec? [ranks]
	(if (= 1 (count ranks)) 
		true
		(and (consec-pair? (first ranks) (second ranks))
	         (consec? (rest ranks)))))

(defn is-straight? [hand] (consec? (ranks hand)))

(defn score [hand]
	(cond
		(is-straight? hand) (->Score :Straight)))

(defn n-tuple-rank [n hand]
	(->> hand
		(group-by :rank)
		(map second)	
		(some #(= n (count %)))
		)
	)

(def pair? (partial n-tuple-rank 2))

(def three? (partial n-tuple-rank 3))

(def four? (partial n-tuple-rank 4))

(defn full-house? [hand] (and (three? hand) (pair? hand)))


(defn two-pairs? [hand] (= 2 (count (->> hand
		(group-by :rank)
		(map second)
		(map count)	
		(filter #(= 2 %))))))

(defn flush? [hand] (= 1 (count (set (map :suit hand)))))

(defn straight-flush? [hand] (and (flush? hand)
	(is-straight? hand)))

(defn compare-high-card-vectors [vector1 vector2]
	#_(if (= 1 (count ranks)) 
		true
		(and (consec-pair? (first ranks) (second ranks))
	         (consec? (rest ranks))))
	(cond 
		(< (first vector1) (first vector2)) false
		(> (first vector1) (first vector2)) true
		:else (compare-high-card-vectors (rest vector1) (rest vector2))))
	
(defn compare-high-card [hand1 hand2]
	(compare-high-card-vectors (ranks hand1) (ranks hand2)))
