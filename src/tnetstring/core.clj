(ns tnetstring.core
  (:use [clojure.string :only [split]]))

;(let [length-str (first (re-seq [#"\d+" str]))]
;   [length-str-length (.length length-str)]
;  (Integer/parseInt length-str)))

(def parsers
  { \# #(Integer/parseInt %)
    \' #(identity %)})

(defn- parse-length [str]
  (let [[length-str remain] (split str #":")]
    [(Integer/parseInt length-str) remain]))

(defn- payload [str len]
    (let [payload-str  (.substring str 0 len)
          remain       (subs str (+ 1 len))
          payload-type (.charAt str len)
          parser       (fn [] (get parsers payload-type))]
      [((parser) payload-str), remain]))

(defn parse-tnetstring
  "Parses a tnetstring"
  [str]
  (let [[payload-len payload-str] (parse-length str)]
    (payload payload-str payload-len)))
