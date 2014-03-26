(ns learningwebgl.lesson-03
  (:require
    [WebGLUtils]
    [mat4]
    [learningwebgl.common :refer [init-gl init-shaders get-perspective-matrix
                                  get-position-matrix deg->rad animate]]
    [cljs-webgl.buffers :refer [create-buffer clear-color-buffer clear-depth-buffer draw!]]
    [cljs-webgl.shaders :refer [get-attrib-location]]
    [cljs-webgl.misc :refer [enable]]
    [cljs-webgl.constants :as const]
    [cljs-webgl.typed-arrays :as ta]))


(defn start []
  (let [canvas      (.getElementById js/document "canvas")
        gl          (init-gl canvas)
        shader-prog (init-shaders gl)
        triangle-vertex-position-buffer
                    (create-buffer gl
                      (ta/float32 [ 0.0,  1.0,  0.0,
                                   -1.0, -1.0,  0.0,
                                    1.0, -1.0,  0.0, ])
                      const/array-buffer
                      const/static-draw
                      3)

        triangle-vertex-color-buffer
                    (create-buffer gl
                      (ta/float32 [ 1.0,  0.0,  0.0,  1.0,
                                    0.0,  1.0,  0.0,  1.0,
                                    0.0,  0.0,  1.0,  1.0 ])
                      const/array-buffer
                      const/static-draw
                      4)

        triangle-matrix (get-position-matrix [-1.5 0.0 -7.0])

        square-vertex-position-buffer
                    (create-buffer gl
                      (ta/float32 [ 1.0,  1.0,  0.0,
                                   -1.0,  1.0,  0.0,
                                    1.0, -1.0,  0.0,
                                   -1.0, -1.0,  0.0])
                      const/array-buffer
                      const/static-draw
                      3)

        square-vertex-color-buffer
                    (create-buffer gl
                      (ta/float32 [ 0.5,  0.5,  1.0,  1.0,
                                    0.5,  0.5,  1.0,  1.0,
                                    0.5,  0.5,  1.0,  1.0,
                                    0.5,  0.5,  1.0,  1.0 ])
                      const/array-buffer
                      const/static-draw
                      4)

        square-matrix (get-position-matrix [ 1.5 0.0 -7.0])

        vertex-position-attribute (get-attrib-location gl shader-prog "aVertexPosition")
        vertex-color-attribute (get-attrib-location gl shader-prog "aVertexColor")
        perspective-matrix (get-perspective-matrix gl)
        one-degree (deg->rad 1)]

    (enable gl const/depth-test)

    (animate
      (fn [frame] ; frame is not used

        (clear-color-buffer gl 0.0 0.0 0.0 1.0)
        (clear-depth-buffer gl 1)

        ; Diverges slightly from the LearningWegGL example:
        ; We just rotate the matrices by 1 degree on each frame

        ; gl-matrix relies on mutating the matrices
        ; ... let's gloss over those details for the moment
        (mat4/rotate
          triangle-matrix
          triangle-matrix
          one-degree
          (ta/float32 [0 1 0]))

        (mat4/rotate
          square-matrix
          square-matrix
          one-degree
          (ta/float32 [1 0 0]))

        (draw!
          gl
          :shader shader-prog
          :draw-mode const/triangles
          :count (.-numItems triangle-vertex-position-buffer)
          :attributes [{:buffer triangle-vertex-position-buffer :location vertex-position-attribute}
                       {:buffer triangle-vertex-color-buffer :location vertex-color-attribute}]
          :uniforms [{:name "uPMatrix" :type :mat4 :values perspective-matrix}
                     {:name "uMVMatrix" :type :mat4 :values triangle-matrix}])

        (draw!
          gl
          :shader shader-prog
          :draw-mode const/triangle-strip
          :count (.-numItems square-vertex-position-buffer)
          :attributes [{:buffer square-vertex-position-buffer :location vertex-position-attribute}
                       {:buffer square-vertex-color-buffer :location vertex-color-attribute}]
          :uniforms [{:name "uPMatrix" :type :mat4 :values perspective-matrix}
                     {:name "uMVMatrix" :type :mat4 :values square-matrix}])))))
