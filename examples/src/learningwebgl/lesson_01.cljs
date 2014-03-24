(ns learningwebgl.lesson-01
  (:require
    [mat4]
    [cljs-webgl.buffers :refer [create-buffer clear-color-buffer draw!]]
    [cljs-webgl.context :refer [get-context]]
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

(defn start []
  (let [canvas      (.getElementById js/document "canvas")
        gl          (init-gl canvas)
        shader-prog (init-shaders gl)
        triangle-vertex-buffer
                    (create-buffer gl
                      (ta/float32 [ 0.0,  1.0,  0.0,
                                   -1.0, -1.0,  0.0,
                                    1.0, -1.0,  0.0 ])
                      const/array-buffer
                      const/static-draw
                      3)

        square-vertex-buffer
                    (create-buffer gl
                      (ta/float32 [ 1.0,  1.0,  0.0,
                                   -1.0,  1.0,  0.0,
                                    1.0, -1.0,  0.0,
                                   -1.0, -1.0,  0.0])
                      const/array-buffer
                      const/static-draw
                      3)

        vertex-position-attribute (get-attrib-location gl shader-prog "aVertexPosition")]

    (clear-color-buffer gl 0.0 0.0 0.0 1.0)
    (.enable gl const/depth-test)

    (draw!
      gl
      shader-prog

      ; draw-mode
      const/triangles

      0 ; first
      (.-numItems triangle-vertex-buffer) ; count

      ; attributes
      [{:buffer triangle-vertex-buffer
        :location vertex-position-attribute}]

      ; uniforms
      [{:name "uPMatrix" :type :mat4 :values (get-perspective-matrix gl)}
       {:name "uMVMatrix" :type :mat4 :values (get-position-matrix [-1.5 0.0 -7.0])}]

      ;element-array
      nil)

    (draw!
      gl
      shader-prog

      ; draw-mode
      const/triangle-strip

      0 ; first
      (.-numItems square-vertex-buffer) ; count

      ; attributes
      [{:buffer square-vertex-buffer
        :location vertex-position-attribute}]

      ; uniforms
      [{:name "uPMatrix" :type :mat4 :values (get-perspective-matrix gl)}
       {:name "uMVMatrix" :type :mat4 :values (get-position-matrix [1.5 0.0 -7.0])}]

      ;element-array
      nil)))
