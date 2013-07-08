(ns cljs-webgl.test.context
  (:require [cljs-webgl.context :as context]))

(defn run []
  (assert (not= nil (context/get-context (.getElementById js/document "canvas")))))
