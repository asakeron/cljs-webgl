(ns cljs-webgl.texture
  (:require
    [cljs-webgl.constants.texture-target :as texture-target]
    [cljs-webgl.constants.texture-parameter-name :as texture-parameter-name]
    [cljs-webgl.constants.webgl :as webgl]
    [cljs-webgl.constants.texture-mag-filter :as texture-mag-filter]
    [cljs-webgl.constants.pixel-format :as pixel-format]
    [cljs-webgl.constants.data-type :as data-type]))

; TODO: probably want to parameterize some of the details here
(defn load-texture
  "Loads the texture from the given URL. Note that the image is loaded in the background,
   and the returned texture will not immediately be fully initialized."
  [gl-context url]
  (let [texture (.createTexture gl-context)
        img (js/Image.)]

    (set! (.-onload img) (fn []
                           (.bindTexture
                             gl-context
                             texture-target/texture-2d
                             texture)

                           (.pixelStorei
                             gl-context
                             webgl/unpack-flip-y-webgl
                             true)

                           (.texImage2D
                             gl-context
                             texture-target/texture-2d
                             0
                             pixel-format/rgba
                             pixel-format/rgba
                             data-type/unsigned-byte
                             img)

                           (.texParameteri
                             gl-context
                             texture-target/texture-2d
                             texture-parameter-name/texture-mag-filter
                             texture-mag-filter/nearest)

                           (.texParameteri
                             gl-context
                             texture-target/texture-2d
                             texture-parameter-name/texture-min-filter
                             texture-mag-filter/nearest)

                           (.bindTexture
                             gl-context
                             texture-target/texture-2d
                             nil)))

    (set! (.-crossOrigin img) "anonymous")
    (set! (.-src img) url)
    texture))
