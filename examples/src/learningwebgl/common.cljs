(ns learningwebgl.common
  (:require
    [mat4]
    [cljs-webgl.buffers :refer [create-buffer clear-color-buffer draw!]]
    [cljs-webgl.context :refer [get-context]]
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
