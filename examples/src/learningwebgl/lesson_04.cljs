(ns learningwebgl.lesson-04
  (:require
    [WebGLUtils]
    [mat4]
    [learningwebgl.common :refer [init-gl init-shaders get-perspective-matrix
                                  get-position-matrix deg->rad animate]]
    [cljs-webgl.buffers :refer [create-buffer clear-color-buffer clear-depth-buffer draw!]]
    [cljs-webgl.shaders :refer [get-attrib-location]]
    [cljs-webgl.misc :refer [capabilities]]
    [cljs-webgl.constants :as const]
    [cljs-webgl.typed-arrays :as ta]))


(defn start []
  (let [canvas      (.getElementById js/document "canvas")
        gl          (init-gl canvas)
        shader-prog (init-shaders gl)
        pyramid-vertex-position-buffer
                    (create-buffer gl
                      (ta/float32 [
                                  ; Front face
                                   0.0,  1.0,  0.0,
                                  -1.0, -1.0,  1.0,
                                   1.0, -1.0,  1.0,

                                  ; Right face
                                   0.0,  1.0,  0.0,
                                   1.0, -1.0,  1.0,
                                   1.0, -1.0, -1.0,

                                  ; Back face
                                   0.0,  1.0,  0.0,
                                   1.0, -1.0, -1.0,
                                  -1.0, -1.0, -1.0,

                                  ; Left face
                                   0.0,  1.0,  0.0,
                                  -1.0, -1.0, -1.0,
                                  -1.0, -1.0,  1.0 ])
                      const/array-buffer
                      const/static-draw
                      3)

        pyramid-vertex-color-buffer
                    (create-buffer gl
                      (ta/float32 [
                                   ; Front face
                                   1.0, 0.0, 0.0, 1.0,
                                   0.0, 1.0, 0.0, 1.0,
                                   0.0, 0.0, 1.0, 1.0,

                                   ; Right face
                                   1.0, 0.0, 0.0, 1.0,
                                   0.0, 0.0, 1.0, 1.0,
                                   0.0, 1.0, 0.0, 1.0,

                                   ; Back face
                                   1.0, 0.0, 0.0, 1.0,
                                   0.0, 1.0, 0.0, 1.0,
                                   0.0, 0.0, 1.0, 1.0,

                                   ; Left face
                                   1.0, 0.0, 0.0, 1.0,
                                   0.0, 0.0, 1.0, 1.0,
                                   0.0, 1.0, 0.0, 1.0 ])
                      const/array-buffer
                      const/static-draw
                      4)

        pyramid-matrix (get-position-matrix [-1.5 0.0 -8.0])

        cube-vertex-position-buffer
                    (create-buffer gl
                      (ta/float32 [
                                  ; Front face
                                  -1.0, -1.0,  1.0,
                                   1.0, -1.0,  1.0,
                                   1.0,  1.0,  1.0,
                                  -1.0,  1.0,  1.0,

                                  ; Back face
                                  -1.0, -1.0, -1.0,
                                  -1.0,  1.0, -1.0,
                                   1.0,  1.0, -1.0,
                                   1.0, -1.0, -1.0,

                                  ; Top face
                                  -1.0,  1.0, -1.0,
                                  -1.0,  1.0,  1.0,
                                   1.0,  1.0,  1.0,
                                   1.0,  1.0, -1.0,

                                  ; Bottom face
                                  -1.0, -1.0, -1.0,
                                   1.0, -1.0, -1.0,
                                   1.0, -1.0,  1.0,
                                  -1.0, -1.0,  1.0,

                                  ; Right face
                                   1.0, -1.0, -1.0,
                                   1.0,  1.0, -1.0,
                                   1.0,  1.0,  1.0,
                                   1.0, -1.0,  1.0,

                                  ; Left face
                                  -1.0, -1.0, -1.0,
                                  -1.0, -1.0,  1.0,
                                  -1.0,  1.0,  1.0,
                                  -1.0,  1.0, -1.0])
                      const/array-buffer
                      const/static-draw
                      3)

        cube-vertex-color-buffer
                    (create-buffer gl
                      (ta/float32 [
                                  ; Front face
                                  1.0, 0.0, 0.0, 1.0,
                                  1.0, 0.0, 0.0, 1.0,
                                  1.0, 0.0, 0.0, 1.0,
                                  1.0, 0.0, 0.0, 1.0,

                                  ; Back face
                                  1.0, 1.0, 0.0, 1.0,
                                  1.0, 1.0, 0.0, 1.0,
                                  1.0, 1.0, 0.0, 1.0,
                                  1.0, 1.0, 0.0, 1.0,

                                  ; Top face
                                  0.0, 1.0, 0.0, 1.0,
                                  0.0, 1.0, 0.0, 1.0,
                                  0.0, 1.0, 0.0, 1.0,
                                  0.0, 1.0, 0.0, 1.0,

                                  ; Bottom face
                                  1.0, 0.5, 0.5, 1.0,
                                  1.0, 0.5, 0.5, 1.0,
                                  1.0, 0.5, 0.5, 1.0,
                                  1.0, 0.5, 0.5, 1.0,

                                  ; Right face
                                  1.0, 0.0, 1.0, 1.0,
                                  1.0, 0.0, 1.0, 1.0,
                                  1.0, 0.0, 1.0, 1.0,
                                  1.0, 0.0, 1.0, 1.0,

                                  ; Left face
                                  0.0, 0.0, 1.0, 1.0,
                                  0.0, 0.0, 1.0, 1.0,
                                  0.0, 0.0, 1.0, 1.0,
                                  0.0, 0.0, 1.0, 1.0 ])
                      const/array-buffer
                      const/static-draw
                      4)

        cube-vertex-indices
                    (create-buffer gl
                      (ta/unsigned-int16 [
                                  0, 1, 2,      0, 2, 3,      ; Front face
                                  4, 5, 6,      4, 6, 7,      ; Back face
                                  8, 9, 10,     8, 10, 11,    ; Top face
                                  12, 13, 14,   12, 14, 15,   ; Bottom face
                                  16, 17, 18,   16, 18, 19,   ; Right face
                                  20, 21, 22,   20, 22, 23])  ; Left face
                      const/element-array-buffer
                      const/static-draw
                      1)

        cube-matrix (get-position-matrix [ 1.5 0.0 -8.0])

        vertex-position-attribute (get-attrib-location gl shader-prog "aVertexPosition")
        vertex-color-attribute (get-attrib-location gl shader-prog "aVertexColor")
        perspective-matrix (get-perspective-matrix gl)
        one-degree (deg->rad 1)]

    (capabilities gl {const/depth-test true})

    (animate
      (fn [frame] ; frame is not used

        (clear-color-buffer gl 0.0 0.0 0.0 1.0)
        (clear-depth-buffer gl 1)

        ; Diverges slightly from the LearningWegGL example:
        ; We just rotate the matrices by 1 degree on each frame

        ; gl-matrix relies on mutating the matrices
        ; ... let's gloss over those details for the moment
        (mat4/rotate
          pyramid-matrix
          pyramid-matrix
          one-degree
          (ta/float32 [0 1 0]))

        (mat4/rotate
          cube-matrix
          cube-matrix
          one-degree
          (ta/float32 [1 1 1]))

        ; TODO: there's no point in building these structures each time
        ;       - should be able to do something like: (apply draw! gl opts)
        (draw!
          gl
          :shader shader-prog
          :draw-mode const/triangles
          :count (.-numItems pyramid-vertex-position-buffer)
          :attributes [{:buffer pyramid-vertex-position-buffer :location vertex-position-attribute}
                       {:buffer pyramid-vertex-color-buffer :location vertex-color-attribute}]
          :uniforms [{:name "uPMatrix" :type :mat4 :values perspective-matrix}
                     {:name "uMVMatrix" :type :mat4 :values pyramid-matrix}])

        (draw!
          gl
          :shader shader-prog
          :draw-mode const/triangles
          :count (.-numItems cube-vertex-indices)
          :attributes [{:buffer cube-vertex-position-buffer :location vertex-position-attribute}
                       {:buffer cube-vertex-color-buffer :location vertex-color-attribute}]
          :uniforms [{:name "uPMatrix" :type :mat4 :values perspective-matrix}
                     {:name "uMVMatrix" :type :mat4 :values cube-matrix}]
          :element-array {:buffer cube-vertex-indices :type const/unsigned-short :offset 0})))))
