(ns cljs-webgl.texture
  (:require
    [cljs-webgl.constants :as constants]))

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
                             constants/texture-2d
                             texture)

                           (.pixelStorei
                             gl-context
                             constants/unpack-flip-y-webgl
                             true)

                           (.texImage2D
                             gl-context
                             constants/texture-2d
                             0
                             constants/rgba
                             constants/rgba
                             constants/unsigned-byte
                             img)

                           (.texParameteri
                             gl-context
                             constants/texture-2d
                             constants/texture-mag-filter
                             constants/nearest)

                           (.texParameteri
                             gl-context
                             constants/texture-2d
                             constants/texture-min-filter
                             constants/nearest)

                           (.bindTexture
                             gl-context
                             constants/texture-2d
                             nil)))

    (set! (.-src img) url)
    texture))
