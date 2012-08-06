(ns tnetstring.core
  (:use [clojure.string :only [split blank?]]))

(declare parse)

(defn- parse-list [str the-list]
  (if (not (blank? str))
    (let [[element str] (parse str)]
      (recur str (conj the-list element)))
    the-list))

(defn- parse-dict [str dict]
  (if (not (blank? str))
    (let [[key value-and-rest] (parse str)]
      (let [[value str] (parse value-and-rest)]
        (recur str (assoc dict key value))))
    dict))

(def parsers
  { \# #(Long/parseLong %)
    \^ #(Double/parseDouble %)
    \! (fn [s] (if (or (= "true" s) (= "false" s))
                       (Boolean/valueOf s)
                       (throw (IllegalArgumentException. "Not a boolean"))))
    \~ (fn [s] (if (not (blank? s))
           (throw (IllegalArgumentException. "Nil does not have a body"))))
    \, #(identity %)
    \] #(parse-list % [])
    \} #(parse-dict % {})})

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

