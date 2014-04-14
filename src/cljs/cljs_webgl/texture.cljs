(ns cljs-webgl.texture
  (:require
    [cljs-webgl.constants.texture-target :as texture-target]
    [cljs-webgl.constants.texture-parameter-name :as texture-parameter-name]
    [cljs-webgl.constants.parameter-name :as parameter-name]
    [cljs-webgl.constants.webgl :as webgl]
    [cljs-webgl.constants.texture-filter :as texture-filter]
    [cljs-webgl.constants.pixel-format :as pixel-format]
    [cljs-webgl.constants.data-type :as data-type]))

(def ^:private default-pixel-store-modes
  "Related OpenGL ES reference pages:
  [glPixelStorei](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glPixelStorei.xml)"
  {webgl/unpack-flip-y-webgl                false
   webgl/unpack-premultiply-alpha-webgl     false
   webgl/unpack-colorspace-conversion-webgl false
   parameter-name/unpack-alignment          4
   parameter-name/pack-alignment            4})


(defn create-texture
  "Valid values for `target` are: `cljs-webgl.constants.texture-target/texture-2d` and
  `texture-cube-map`. The default value is `texture-2d`."
  [gl-context & {:keys [image target pixel-store-modes generate-mipmaps?
                        level-of-detail internal-pixel-format pixel-format
                        data-type parameters] :as opts}]
  (let [texture (.createTexture gl-context)
        target (or target texture-target/texture-2d)
        pixel-store-modes (merge default-pixel-store-modes pixel-store-modes)]

    (.bindTexture gl-context target texture)

    (doseq [[parameter value] pixel-store-modes]
      (.pixelStorei gl-context parameter value))

    (.texImage2D
      gl-context
      target
      (or level-of-detail 0)
      (or internal-pixel-format pixel-format/rgba)
      (or pixel-format pixel-format/rgba)
      data-type/unsigned-byte
      image)

    (doseq [[k v] parameters]
      (.texParameteri gl-context target k v))

    (when generate-mipmaps?
      (.generateMipmap gl-context target))

    (.bindTexture gl-context target nil)

    texture))
