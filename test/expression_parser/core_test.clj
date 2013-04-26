(ns expression-parser.core-test
  (:use clojure.test
        expression-parser.core))

(deftest it-should-correctly-parse-the-expression
  (testing "it should correctly parse the expression"
    (is (= 32 (do-expression "(((1 + 3) * 7) + ((((3 - 1) / (1 + 3)) * 8)))")))))
