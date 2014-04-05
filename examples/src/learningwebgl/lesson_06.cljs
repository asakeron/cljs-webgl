(ns learningwebgl.lesson-06
  (:require
    [WebGLUtils]
    [mat4]
    [learningwebgl.common :refer [init-gl init-shaders get-perspective-matrix
                                  get-position-matrix deg->rad animate]]
    [cljs-webgl.buffers :refer [create-buffer clear-color-buffer clear-depth-buffer draw!]]
    [cljs-webgl.shaders :refer [get-attrib-location]]
    [cljs-webgl.constants.buffer-object :as buffer-object]
    [cljs-webgl.constants.capability :as capability]
    [cljs-webgl.constants.draw-mode :as draw-mode]
    [cljs-webgl.constants.data-type :as data-type]
    [cljs-webgl.constants.texture-parameter-name :as texture-parameter-name]
    [cljs-webgl.constants.texture-filter :as texture-filter]
    [cljs-webgl.texture :refer [load-image create-texture init-texture]]
    [cljs-webgl.typed-arrays :as ta]))

; TODO: get rid of atom; use big-bang or incorporate state into animate fn
(def state
  (atom {:x-rotation 0
         :y-rotation 0
         :x-speed 1
         :y-speed -2
         :z-depth -5.0
         :filter 0}))

(defn init-textures [gl url]
  (let [tex1 (create-texture gl)
        tex2 (create-texture gl)
        tex3 (create-texture gl)]

    (load-image
      url
      (fn [img]
        (init-texture gl
          :image img
          :texture tex1
          :parameters {texture-parameter-name/texture-mag-filter texture-filter/nearest
                       texture-parameter-name/texture-min-filter texture-filter/nearest})

        (init-texture gl
          :image img
          :texture tex2
          :parameters {texture-parameter-name/texture-mag-filter texture-filter/linear
                       texture-parameter-name/texture-min-filter texture-filter/linear})

        (init-texture gl
          :image img
          :texture tex3
          :parameters {texture-parameter-name/texture-mag-filter texture-filter/linear
                       texture-parameter-name/texture-min-filter texture-filter/linear-mipmap-nearest}
          :generate-mipmap true)))

    [tex1 tex2 tex3]))

(defn key-down-handler [event]
  (let [key-code (.-keyCode event)]

    (when (= key-code 70) ; F
      (swap! state update-in [:filter] #(mod (inc %) 3)))

    (when (= key-code 33) ; Page-Up
      (swap! state update-in [:z-depth] #(- % 0.05)))

    (when (= key-code 34) ; Page-Down
      (swap! state update-in [:z-depth] #(+ % 0.05)))

    (when (= key-code 37) ; Left
      (swap! state update-in [:y-speed] dec))

    (when (= key-code 39) ; Right
      (swap! state update-in [:y-speed] inc))

    (when (= key-code 38) ; Up
      (swap! state update-in [:x-speed] dec))

    (when (= key-code 40) ; Down
      (swap! state update-in [:x-speed] inc))

    ; prevent default?
    (not (contains? #{70 33 34 37 39 38 40} key-code))))

(defn update-rotation []
 (swap! state update-in [:x-rotation] + (* 0.1 (:x-speed @state)))
 (swap! state update-in [:y-rotation] + (* 0.1 (:y-speed @state))))

(defn ^:export start []
  (let [canvas      (.getElementById js/document "canvas")
        gl          (init-gl canvas)
        shader-prog (init-shaders gl)
        crate-textures (init-textures gl "crate.gif")

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
                      buffer-object/array-buffer
                      buffer-object/static-draw
                      3)

        cube-vertex-texture-coords-buffer
                    (create-buffer gl
                      (ta/float32 [
                                  ; Front face
                                  0.0, 0.0,
                                  1.0, 0.0,
                                  1.0, 1.0,
                                  0.0, 1.0,

                                  ; Back face
                                  1.0, 0.0,
                                  1.0, 1.0,
                                  0.0, 1.0,
                                  0.0, 0.0,

                                  ; Top face
                                  0.0, 1.0,
                                  0.0, 0.0,
                                  1.0, 0.0,
                                  1.0, 1.0,

                                  ; Bottom face
                                  1.0, 1.0,
                                  0.0, 1.0,
                                  0.0, 0.0,
                                  1.0, 0.0,

                                  ; Right face
                                  1.0, 0.0,
                                  1.0, 1.0,
                                  0.0, 1.0,
                                  0.0, 0.0,

                                  ; Left face
                                  0.0, 0.0,
                                  1.0, 0.0,
                                  1.0, 1.0,
                                  0.0, 1.0, ])
                      buffer-object/array-buffer
                      buffer-object/static-draw
                      2)

        cube-vertex-indices
                    (create-buffer gl
                      (ta/unsigned-int16 [
                                  0, 1, 2,      0, 2, 3,      ; Front face
                                  4, 5, 6,      4, 6, 7,      ; Back face
                                  8, 9, 10,     8, 10, 11,    ; Top face
                                  12, 13, 14,   12, 14, 15,   ; Bottom face
                                  16, 17, 18,   16, 18, 19,   ; Right face
                                  20, 21, 22,   20, 22, 23])  ; Left face
                      buffer-object/element-array-buffer
                      buffer-object/static-draw
                      1)

        cube-matrix (mat4/create)

        vertex-position-attribute (get-attrib-location gl shader-prog "aVertexPosition")
        texture-coord-attribute   (get-attrib-location gl shader-prog "aTextureCoord")
        perspective-matrix (get-perspective-matrix gl)]

    (set! (.-onkeydown js/document) key-down-handler)

    (animate
      (fn [frame] ; frame is not used

        (clear-color-buffer gl 0.0 0.0 0.0 1.0)
        (clear-depth-buffer gl 1)

        (update-rotation)

        (mat4/identity
          cube-matrix)

        (mat4/translate
          cube-matrix
          cube-matrix
          (ta/float32 [0 0 (:z-depth @state)]))

        (mat4/rotate
          cube-matrix
          cube-matrix
          (deg->rad (:x-rotation @state))
          (ta/float32 [1 0 0]))

        (mat4/rotate
          cube-matrix
          cube-matrix
          (deg->rad (:y-rotation @state))
          (ta/float32 [0 1 0]))

        (draw!
          gl
          :shader shader-prog
          :draw-mode draw-mode/triangles
          :count (.-numItems cube-vertex-indices)
          :attributes [{:buffer cube-vertex-position-buffer :location vertex-position-attribute}
                       {:buffer cube-vertex-texture-coords-buffer :location texture-coord-attribute}]
          :uniforms [{:name "uPMatrix" :type :mat4 :values perspective-matrix}
                     {:name "uMVMatrix" :type :mat4 :values cube-matrix}]
          :textures [{:name "uSampler" :texture (crate-textures (:filter @state))}]
          :element-array {:buffer cube-vertex-indices :type data-type/unsigned-short :offset 0}
          :capabilities {capability/depth-test true})))))
