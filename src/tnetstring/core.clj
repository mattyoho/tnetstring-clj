(ns tnetstring.core
  (:use [clojure.string :only [split blank?]]))

(declare parse)

(defn- parse-list [str the-list]
  (if (not (blank? str))
    (let [[element str] (parse str)]
      (recur str (conj the-list element)))
    the-list))

(def parsers
  { \# #(Integer/parseInt %)
    \^ #(Float/parseFloat %)
    \! (fn [s] (if (or (= "true" s) (= "false" s))
                       (Boolean/valueOf s)
                       (throw (IllegalArgumentException. "Not a boolean"))))
    \~ (fn [s] (if (not (blank? s))
           (throw (IllegalArgumentException. "Nil does not have a body"))))
    \, #(identity %)
    \] #(parse-list % [])})

(defn- parse-length [str]
  (let [[length-str remain] (split str #":" 2)]
    [(Integer/parseInt length-str) remain]))

(defn- payload [str len]
    (let [payload-str  (.substring str 0 len)
          remain       (subs str (+ 1 len))
          payload-type (.charAt str len)
          parser       (fn [] (get parsers payload-type))]
      [((parser) payload-str), remain]))

(defn parse
  "Parses a tnetstring"
  [str]
  (let [[payload-len payload-str] (parse-length str)]
    (payload payload-str payload-len)))

