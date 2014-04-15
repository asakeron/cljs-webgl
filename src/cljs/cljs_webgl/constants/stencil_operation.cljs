(ns cljs-webgl.constants.stencil-operation
  (:refer-clojure :exclude [keep
                            replace]))

(def keep      0x1E00)
(def replace   0x1E01)
(def incr      0x1E02)
(def decr      0x1E03)
(def invert    0x150A)
(def incr-wrap 0x8507)
(def decr-wrap 0x8508)
