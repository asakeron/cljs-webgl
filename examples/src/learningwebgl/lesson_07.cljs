(ns learningwebgl.lesson-07
  (:require
    [WebGLUtils]
    [vec3]
    [mat3]
    [mat4]
    [learningwebgl.common :refer [init-gl init-shaders get-perspective-matrix
                                  get-position-matrix deg->rad animate load-image]]
    [cljs-webgl.buffers :refer [create-buffer clear-color-buffer clear-depth-buffer draw!]]
    [cljs-webgl.shaders :refer [get-attrib-location]]
    [cljs-webgl.constants.buffer-object :as buffer-object]
    [cljs-webgl.constants.capability :as capability]
    [cljs-webgl.constants.draw-mode :as draw-mode]
    [cljs-webgl.constants.data-type :as data-type]
    [cljs-webgl.constants.texture-parameter-name :as texture-parameter-name]
    [cljs-webgl.constants.texture-filter :as texture-filter]
    [cljs-webgl.constants.webgl :as webgl]
    [cljs-webgl.texture :refer [create-texture]]
    [cljs-webgl.typed-arrays :as ta]))

; TODO: get rid of atom; use big-bang or incorporate state into animate fn
(def state
  (atom {:x-rotation 0
         :y-rotation 0
         :x-speed 1
         :y-speed -2
         :z-depth -5.0
         :keypresses {}
         :filter 0}))

(defn init-textures [gl url callback]
  (load-image
   url
   (fn [img]
     (let [tex1 (create-texture gl
                  :image img
                  :pixel-store-modes {webgl/unpack-flip-y-webgl true}
                  :parameters {texture-parameter-name/texture-mag-filter texture-filter/nearest
                               texture-parameter-name/texture-min-filter texture-filter/nearest})
           tex2 (create-texture gl
                  :image img
                  :pixel-store-modes {webgl/unpack-flip-y-webgl true}
                  :parameters {texture-parameter-name/texture-mag-filter texture-filter/linear
                               texture-parameter-name/texture-min-filter texture-filter/linear})
           tex3 (create-texture gl
                  :image img
                  :pixel-store-modes {webgl/unpack-flip-y-webgl true}
                  :parameters {texture-parameter-name/texture-mag-filter texture-filter/linear
                               texture-parameter-name/texture-min-filter texture-filter/linear-mipmap-nearest}
                  :generate-mipmaps? true)]
       (callback [tex1 tex2 tex3])))))

(defn key-down-handler [event]
  (let [key-code (.-keyCode event)]

    (swap! state assoc-in [:keypresses key-code] true)

    (when (= key-code 70) ; F
      (swap! state update-in [:filter] #(mod (inc %) 3)))

    ; prevent default? (allow left/right)
    (not (contains? #{70 33 34 38 40} key-code))))

(defn key-up-handler [event]
  (let [key-code (.-keyCode event)]
    (swap! state assoc-in [:keypresses key-code] false)))

(defn input-handler []
  (let [key-code (:keypresses @state)]

    (when (key-code 33) ; Page-Up
      (swap! state update-in [:z-depth] #(- % 0.05)))

    (when (key-code 34) ; Page-Down
      (swap! state update-in [:z-depth] #(+ % 0.05)))

    (when (key-code 37) ; Left
      (swap! state update-in [:y-speed] dec))

    (when (key-code 39) ; Right
      (swap! state update-in [:y-speed] inc))

    (when (key-code 38) ; Up
      (swap! state update-in [:x-speed] dec))

    (when (key-code 40) ; Down
      (swap! state update-in [:x-speed] inc))))

(defn update-rotation []
 (swap! state update-in [:x-rotation] + (* 0.1 (:x-speed @state)))
 (swap! state update-in [:y-rotation] + (* 0.1 (:y-speed @state))))

(defn get-float [element-id]
  (js/parseFloat
    (.-value
      (.getElementById
        js/document
        element-id))))

(defn ambient-color []
  {:name "uAmbientColor"
   :type :vec3
   :values (ta/float32 [
              (get-float "ambientR")
              (get-float "ambientG")
              (get-float "ambientB")])})

(defn directional-color []
  {:name "uDirectionalColor"
   :type :vec3
   :values (ta/float32 [
              (get-float "directionalR")
              (get-float "directionalG")
              (get-float "directionalB")])})

(defn lighting-direction []
  (let [lighting-dir (ta/float32 [
                        (get-float "lightDirectionX")
                        (get-float "lightDirectionY")
                        (get-float "lightDirectionZ")])
        adjusted-dir (vec3/create)]

    (vec3/normalize adjusted-dir lighting-dir)
    (vec3/scale adjusted-dir adjusted-dir -1.0)

    {:name "uLightingDirection"
     :type :vec3
     :values adjusted-dir}))

(defn lighting []
  (let [use-lighting? (.-checked (.getElementById js/document "lighting"))]
    (cons
      {:name "uUseLighting" :type :int :values (ta/unsigned-int32 [use-lighting?])}
      (when use-lighting?
        [(ambient-color) (lighting-direction) (directional-color) ]))))

(defn ^:export start []
  (let [canvas      (.getElementById js/document "canvas")
        gl          (init-gl canvas)
        shader-prog (init-shaders gl)
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

        cube-vertex-normal-buffer
                    (create-buffer gl
                      (ta/float32 [
                                  ; Front face
                                   0.0,  0.0,  1.0,
                                   0.0,  0.0,  1.0,
                                   0.0,  0.0,  1.0,
                                   0.0,  0.0,  1.0,

                                  ; Back face
                                   0.0,  0.0, -1.0,
                                   0.0,  0.0, -1.0,
                                   0.0,  0.0, -1.0,
                                   0.0,  0.0, -1.0,

                                  ; Top face
                                   0.0,  1.0,  0.0,
                                   0.0,  1.0,  0.0,
                                   0.0,  1.0,  0.0,
                                   0.0,  1.0,  0.0,

                                  ; Bottom face
                                   0.0, -1.0,  0.0,
                                   0.0, -1.0,  0.0,
                                   0.0, -1.0,  0.0,
                                   0.0, -1.0,  0.0,

                                  ; Right face
                                   1.0,  0.0,  0.0,
                                   1.0,  0.0,  0.0,
                                   1.0,  0.0,  0.0,
                                   1.0,  0.0,  0.0,

                                  ; Left face
                                  -1.0,  0.0,  0.0,
                                  -1.0,  0.0,  0.0,
                                  -1.0,  0.0,  0.0,
                                  -1.0,  0.0,  0.0])
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

        normal-matrix (mat3/create)

        vertex-position-attribute (get-attrib-location gl shader-prog "aVertexPosition")
        vertex-normal-attribute   (get-attrib-location gl shader-prog "aVertexNormal")
        texture-coord-attribute   (get-attrib-location gl shader-prog "aTextureCoord")
        perspective-matrix (get-perspective-matrix gl)]

    (set! (.-onkeydown js/document) key-down-handler)
    (set! (.-onkeyup js/document) key-up-handler)

    (init-textures gl "crate.gif" (fn [crate-textures]
      (animate
        (fn [frame] ; frame is not used

          (clear-color-buffer gl 0.0 0.0 0.0 1.0)
          (clear-depth-buffer gl 1)

          (input-handler)
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

          (mat3/normalFromMat4
            normal-matrix
            cube-matrix)

          (draw!
            gl
            :shader shader-prog
            :draw-mode draw-mode/triangles
            :count (.-numItems cube-vertex-indices)
            :attributes [{:buffer cube-vertex-position-buffer :location vertex-position-attribute}
                         {:buffer cube-vertex-normal-buffer :location vertex-normal-attribute}
                         {:buffer cube-vertex-texture-coords-buffer :location texture-coord-attribute}]
            :uniforms (concat
                        [{:name "uPMatrix" :type :mat4 :values perspective-matrix}
                         {:name "uMVMatrix" :type :mat4 :values cube-matrix}
                         {:name "uNMatrix" :type :mat3 :values normal-matrix}]
                        (lighting))
            :textures [{:name "uSampler" :texture (crate-textures (:filter @state))}]
            :element-array {:buffer cube-vertex-indices :type data-type/unsigned-short :offset 0}
            :capabilities {capability/depth-test true})))))))
