(ns tnetstring.core
  (:use [clojure.string :only [split blank?]]))

(declare parse)

(defn- parse-list [s the-list]
  (if (not (blank? s))
    (let [[element s] (parse s)]
      (recur s (conj the-list element)))
    the-list))

(defn- parse-dict [s dict]
  (if (not (blank? s))
    (let [[key value-and-rest] (parse s)]
      (let [[value s] (parse value-and-rest)]
        (recur s (assoc dict key value))))
    dict))

(defn- parse-length [s]
  (let [[length-str remain] (split s #":" 2)]
    [(Integer/parseInt length-str) remain]))

(let [parsers
  { \# #(Long/parseLong %)
    \^ #(Double/parseDouble %)
    \! (fn [s] (if (or (= "true" s) (= "false" s))
                       (Boolean/valueOf s)
                       (throw (IllegalArgumentException. "Not a boolean"))))
    \~ (fn [s] (if (not (blank? s))
           (throw (IllegalArgumentException. "Nil does not have a body"))))
    \, #(identity %)
    \] #(parse-list % [])
    \} #(parse-dict % {})}]

    (defn- payload [s len]
        (let [payload-str  (.substring s 0 len)
              remain       (subs s (+ 1 len))
              payload-type (.charAt s len)
              parser       (fn [] (get parsers payload-type))]
          [((parser) payload-str), remain])))

(defn parse
  "Parses a tnetstring"
  [s]
  (let [[payload-len payload-str] (parse-length s)]
    (payload payload-str payload-len)))

