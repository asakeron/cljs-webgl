(ns learningwebgl.common
  (:require
    [vec3]
    [mat4]
    [cljs-webgl.buffers :refer [create-buffer clear-color-buffer draw!]]
    [cljs-webgl.context :refer [get-context get-viewport]]
    [cljs-webgl.shaders :refer [get-shader create-program get-attrib-location]]
    [cljs-webgl.texture :refer [create-texture]]
    [cljs-webgl.constants.texture-parameter-name :as texture-parameter-name]
    [cljs-webgl.constants.texture-filter :as texture-filter]
    [cljs-webgl.constants.webgl :as webgl]
    [cljs-webgl.typed-arrays :as ta]))

(enable-console-print!)

(defn init-gl [canvas]
  (let [gl (get-context canvas)]
    (when-not gl
      (throw (js/Error. "Could not initialize WebGL")))
    gl))

(defn init-shaders [gl]
  (let [fragment-shader (get-shader gl "shader-fs")
        vertex-shader (get-shader gl "shader-vs")]
    (create-program gl fragment-shader vertex-shader)))

(defn get-perspective-matrix [gl]
  (let [{viewport-width :width,
         viewport-height :height} (get-viewport gl)]
    (mat4/perspective
      (mat4/create)
      45
      (/  viewport-width viewport-height)
      0.1
      100.0)))

(defn get-position-matrix [v]
  (let [m (mat4/create)]
    (mat4/identity m)
    (mat4/translate m m (clj->js v))))

(defn deg->rad [degrees]
  (/ (* degrees Math/PI) 180))

(defn animate [draw-fn]
  (letfn [(loop [frame]
            (fn []
              (.requestAnimFrame  js/window (loop (inc frame)))
              (draw-fn frame)))]
    ((loop 0))))

(defn load-image
  ""
  [url callback-fn]
  (let [img (js/Image.)]
    (set! (.-onload img) (fn [] (callback-fn img)))
    (set! (.-crossOrigin img) "anonymous")
    (set! (.-src img) url)))

; TODO: probably want to parameterize some of the details here
; TODO: deprecate this method?
(defn load-texture
  "Loads the texture from the given URL. Note that the image is loaded in the background,
   and the returned texture will not immediately be fully initialized."
  [gl-context url callback-fn]
  (load-image url (fn [img] (callback-fn
                              (create-texture
                                gl-context
                                :image img
                                :pixel-store-modes {webgl/unpack-flip-y-webgl true}
                                :parameters {texture-parameter-name/texture-mag-filter texture-filter/nearest
                                             texture-parameter-name/texture-min-filter texture-filter/nearest})))))

(defn checked? [element-id]
  (.-checked
    (.getElementById
      js/document
      element-id)))

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

(defn blending [use-blending?]
  (when use-blending?
    [{:name "uAlpha"
      :type :float
      :values (ta/float32 [(get-float "alpha")])}]))

(defn lighting [use-lighting?]
  (cons
    {:name "uUseLighting" :type :int :values (ta/int32 [(if use-lighting? 1 0)])}
    (when use-lighting?
      [(ambient-color) (lighting-direction) (directional-color) ])))
