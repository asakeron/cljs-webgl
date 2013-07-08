(ns cljs-webgl.test
  (:require [cljs-webgl.test.context :as context]))

(def success 0)

(defn ^:export run []
  (.log js/console "Example test started.")
  (context/run)
  success)
