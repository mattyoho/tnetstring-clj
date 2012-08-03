(ns tnetstring.core-test
  (:use clojure.test
        tnetstring.core))

(deftest parse-a-number
  (testing "Parsing a single number"
    (is (= [2, ""] (parse-tnetstring "1:2#")))
    (is (= [14, ""] (parse-tnetstring "2:14#")))))

(deftest parse-a-float
  (testing "Parsing a floating point number (Who says you can't compare floats for equality?)"
    (is (= [(float 2.5), ""] (parse-tnetstring "3:2.5^")))
    (is (= [(float 3.14), ""] (parse-tnetstring "4:3.14^")))))

(deftest parse-a-string
  (testing "Parsing an empty string"
    (is (= ["", ""] (parse-tnetstring "0:'"))))
  (testing "Parsing a string"
    (is (= ["foo", ""] (parse-tnetstring "3:foo'")))))
