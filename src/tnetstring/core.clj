(ns tnetstring.core
  (:use [clojure.string :only [split]]))

(def parsers
  { \# #(Integer/parseInt %)
    \^ #(Float/parseFloat %)
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

