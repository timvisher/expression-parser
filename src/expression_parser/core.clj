(ns expression-parser.core)

(defn simple-expression? [expression]
  (if (vector? expression)
    (every? (complement vector?) expression)))

(defn calc-simple-expression [simple-expression]
  ;; We only know how to deal with simple expressions ([1 + 3], not [[1 + 3] * 7]
  {:pre [simple-expression?]}
  (if (= 1 (count simple-expression))
    ;; Takes care of popping [1] => 1
    (first simple-expression)
    ;; Takes care of doing the unary op
    (let [[arg1 op arg2] simple-expression]
      ((resolve op) arg1 arg2))))

(defn try-calc-simple-expression [vector]
  (if (simple-expression? vector)
    (calc-simple-expression vector)
    ;; Need to return the vector so that we can prewalk it again next time
    vector))

(defn expression-str->expression-tree [expression-str]
  ;; Constructing a tree is hard! Let's let the reader do it!
  (let [vectorized-expression-chars  (replace {\( \[ \) \]} expression-str)
        vectorized-expression-string (apply str vectorized-expression-chars)
        expression-tree              (read-string vectorized-expression-string)]
    expression-tree))

(defn calc-expression [expression]
  (if (vector? expression)
    ;; Still a vector? No problem, keep recurring!
    (recur (clojure.walk/prewalk try-calc-simple-expression expression))
    ;; Looks like we're done!
    expression))

(defn do-expression
  "I calculate unary calculation strings and give you the result."
  [expression-string]
  (let [expression-tree (expression-str->expression-tree expression-string)]
    (calc-expression expression-tree)))
