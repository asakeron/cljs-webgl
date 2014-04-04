(ns cljs-webgl.texture
  (:require
    [cljs-webgl.constants.texture-target :as texture-target]
    [cljs-webgl.constants.texture-parameter-name :as texture-parameter-name]
    [cljs-webgl.constants.webgl :as webgl]
    [cljs-webgl.constants.texture-mag-filter :as texture-mag-filter]
    [cljs-webgl.constants.pixel-format :as pixel-format]
    [cljs-webgl.constants.data-type :as data-type]))

(defn create-texture
  " Relevant OpenGL ES reference pages:

   * [glGenTextures](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glGenTextures.xml)"
  [gl-context]
  (.createTexture gl-context))

(defn init-texture
  [gl-context & {:keys [image texture generate-mipmap parameters] :as opts}]
  (let [texture (or texture (.createTexture gl-context))]
    (.bindTexture gl-context texture-target/texture-2d texture)

    (.pixelStorei gl-context webgl/unpack-flip-y-webgl true)

    (.texImage2D
      gl-context
      texture-target/texture-2d
      0
      pixel-format/rgba
      pixel-format/rgba
      data-type/unsigned-byte
      image)

    (doseq [[k v] parameters]
      (.texParameteri gl-context texture-target/texture-2d k v))

    (when generate-mipmap
      (.generateMipmap gl-context texture-target/texture-2d))

    (.bindTexture gl-context texture-target/texture-2d nil)

    texture))

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
  [gl-context url]
  (let [texture (create-texture gl-context)]
    (load-image url (fn [img]
                      (init-texture gl-context
                        :image img
                        :texture texture
                        :parameters {texture-parameter-name/texture-mag-filter texture-mag-filter/nearest
                                     texture-parameter-name/texture-min-filter texture-mag-filter/nearest})))
    texture))

