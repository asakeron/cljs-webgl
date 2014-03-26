(ns learningwebgl.common
  (:require
    [mat4]
    [cljs-webgl.buffers :refer [create-buffer clear-color-buffer draw!]]
    [cljs-webgl.context :refer [get-context]]
    [cljs-webgl.misc :refer [enable]]
    [cljs-webgl.shaders :refer [get-shader create-program get-attrib-location]]
    [cljs-webgl.constants :as const]
    [cljs-webgl.typed-arrays :as ta]))

(enable-console-print!)

(defn init-gl [canvas]
  (let [gl (get-context canvas)]
    (when-not gl
      (throw (js/Error. "Could not initialize WebGL")))

    (set! (.-viewportWidth gl) (.-width canvas))
    (set! (.-viewportHeight gl) (.-height canvas))
    gl))

(defn init-shaders [gl]
  (let [fragment-shader (get-shader gl "shader-fs")
        vertex-shader (get-shader gl "shader-vs")]
    (create-program gl fragment-shader vertex-shader)))

(defn get-perspective-matrix [gl]
  (mat4/perspective
    (mat4/create)
    45
    (/  (.-viewportWidth gl) (.-viewportHeight gl))
    0.1
    100.0))

(defn get-position-matrix [v]
  (let [m (mat4/create)]
    (mat4/identity m)
    (mat4/translate m m (clj->js v))))
