(ns tnetstring.core-test
  (:use clojure.test
        tnetstring.core))

(deftest parse-a-number
  (testing "Parsing a single number"
    (is (= [2, ""] (parse "1:2#")))
    (is (= [14, ""] (parse "2:14#")))))

(deftest parse-a-float
  (testing "Parsing a floating point number (Who says you can't compare floats for equality?)"
    (is (= [(float 2.5), ""] (parse "3:2.5^")))
    (is (= [(float 3.14), ""] (parse "4:3.14^")))))

(deftest parse-booleans
  (testing "Parsing a boolean value"
    (is (= [true, ""] (parse "4:true!")))
    (is (= [false, ""] (parse "5:false!")))
    (is (thrown? IllegalArgumentException (parse "5:flase!")))))

(deftest parse-nil
  (testing "Parsing a nil"
    (is (= [nil, ""] (parse "0:~")))
    (is (thrown? IllegalArgumentException (parse "1:n~")))))

(deftest parse-list
  (testing "Parsing an empty list"
    (is (= [[] ""] (parse "0:]"))))
  (testing "Parsing a list of elements"
    (is (= [[12345 67890 "xxxxx"] ""] (parse "24:5:12345#5:67890#5:xxxxx,]")))))

(deftest parse-a-string
  (testing "Parsing an empty string"
    (is (= ["", ""] (parse "0:,"))))
  (testing "Parsing a string"
    (is (= ["foo", ""] (parse "3:foo,")))))
