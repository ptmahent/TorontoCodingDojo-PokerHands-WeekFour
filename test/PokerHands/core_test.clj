(ns PokerHands.core-test
  (:use clojure.test
        PokerHands.core))

(deftest a-card-test
	(is (= 2 (parse-rank \2)))
	(is (= 5 (parse-rank \5)))
	(is (= 10 (parse-rank \T)))
	(is (= :Hearts (parse-suit \H)))
	(is (= :Diamonds (parse-suit \D)))
	(is (= (->Card 14 :Hearts) (c "AH")))
	(is (= (->Card 10 :Spades) (c "TS"))))

(deftest a-hand-test
	(is (= [ (c "AH") (c "KD") (c "2C")]
		(hand "AH KD 2C")))
	(is (= [ (c "5H") (c "JS") (c "2C") ]
		(hand "5H JS 2C"))))

(deftest ranks-from-hand
	(is (= [14 13 11 4 3] (ranks (hand "AH KS JC 3D 4H"))))
	(is (= [14 12 11 4 2] (ranks (hand "AH QS JC 2D 4H")))))

(deftest straight-tests
	(is (= true (consec-pair? 4 3)))
	(is (= false (consec-pair? 3 4)))   ; we expect descending order
	(is (= false (consec-pair? 11 2)))
	(is (= true (consec? [11 10 9])))
	(is (= false (consec? [11 10 5])))
	(is (= true (consec? [11 10])))
	(is (= false (consec? [11 9])))
	(is (is-straight? (hand "AH KS QD JC TH")))
	(is (not (is-straight? (hand "AH KS QD 3C 2H")))))

(deftest pair-tests
	(is (pair? (hand "7H 7D 4H 3H 2H")))
	(is (not (pair? (hand "7H 6D 4H 3H 2H"))))
	(is (not (pair? (hand "7H 7D 7H 3H 2H")))))

(deftest three-of-a-kind-tests
	(is (not (three? (hand "7H 7D 6H 3H 2H"))))
	(is	(three? (hand "7H 7D 7C 3H 2H"))))

(deftest four-of-a-kind-tests
	(is (not (four? (hand "7H 7D 6H 3H 2H"))))
	(is (four? (hand "7H 7D 7H 7C 2H"))))

(deftest full-house-tests
	(is (not (full-house? (hand "7H 7D 6H 3H 2H"))))
	(is (not (full-house? (hand "7H 7D 7H 3H 2H"))))
	(is (full-house? (hand "7H 7D 7H 2C 2H"))))


(deftest score-tests
	(is (= (->Score :Straight) 
		(score (hand "9H 8S 7D 6C 5H"))))
	(is (not (= (->Score :Straight) 
		(score (hand "7H 5H 4H 3H 2H"))))))

(deftest two-pairs-tests
	(is (not (two-pairs? (hand "2H 2S 3D 4H 5S"))))
	(is (two-pairs? (hand "2H 2S 3D 3H 4S"))))

(deftest flush-tests
	(is (not (flush? (hand "2H 3H 4H 5H 5D"))))
	(is (flush? (hand "2H 3H 4H 5H 6H"))))

(deftest straight-flush-tests
	(is (straight-flush? (hand "2H 3H 4H 5H 6H")))
	(is (not (straight-flush? (hand "2H 2S 4H 5H 6H"))))
	(is (not (straight-flush? (hand "KH 3H 4H 5H 6H")))))

(deftest compare-high-card-test
	(is (compare-high-card 
			(hand "2H 2S 2D 3H 9H") 
			(hand "3S 4S 5S 6S 7S")))
	(is (compare-high-card 
			(hand "2H 2S 2D 6H 9H") 
			(hand "3S 4S 5S 3S 9S")))
	(is (not (compare-high-card
			(hand "2H 2S 2D 3H 2H")
			(hand "3S 4S 5S 6S 7S")))))

(deftest compare-vectors-high-card-test
	(is (compare-high-card-vectors [5 4 3 2 1] [4 4 3 2 1]))
	(is (not (compare-high-card-vectors [5 4 3 2 1] [6 4 3 2 1])))
	(is (compare-high-card-vectors [5 5 3 2 1] [5 4 3 2 1]))
	(is (not (compare-high-card-vectors [5 4 3 2 1] [5 5 3 2 1]))))
