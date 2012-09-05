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
	(is (is-straight? (hand "AH KS QD JC TH")))
	(is (not (is-straight? (hand "AH KS QD 3C 2H")))))

(deftest score-tests
	(is (= (->Score :Straight) 
		(score (hand "9H 8S 7D 6C 5H"))))
	(is (not (= (->Score :Straight) 
		(score (hand "7H 5H 4H 3H 2H")))))
	)
