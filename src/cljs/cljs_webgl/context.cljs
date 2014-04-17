;; Contains functions for querying information from a WebGL context.
(ns cljs-webgl.context
  (:require [cljs-webgl.constants.parameter-name :as parameter-name]))

(defn get-context
  "Gets a WebGL context from a canvas element.
`context-attributes` may be a map in the following form:

    {:alpha
     :depth
     :stencil
     :antialias
     :premultiplied-apha
     :preserve-drawing-buffer
     :prefer-low-power-to-high-performance
     :fail-if-major-performance-caveat}
If you don't specify any key, the default value is assumed.

For further information on context creation parameters see [WebGLContextAttributes](https://www.khronos.org/registry/webgl/specs/1.0.2/#WEBGLCONTEXTATTRIBUTES);"
  ([canvas-element]
     (get-context canvas-element {}))
  ([canvas-element context-attributes]
     (let [default-attributes {:alpha                                true
                               :depth                                true,
                               :stencil                              false,
                               :antialias                            true,
                               :premultiplied-alpha                  true,
                               :preserve-drawing-buffer              false,
                               :prefer-low-power-to-high-performance false,
                               :fail-if-major-performance-caveat     false}
           attributes->js (fn [{:keys [alpha depth stencil antialias premultiplied-alpha preserve-drawing-buffer
                                       prefer-low-power-to-high-performance fail-if-major-performance-caveat]}]
                            (clj->js
                              {:alpha alpha,
                               :depth depth,
                               :stencil stencil,
                               :antialias antialias,
                               :premultipliedAlpha premultiplied-alpha,
                               :preserveDrawingBuffer preserve-drawing-buffer,
                               :preferLowPowerToHighPerformance prefer-low-power-to-high-performance,
                               :failIfMajorPerformanceCaveat fail-if-major-performance-caveat}))
           opts (attributes->js (merge default-attributes context-attributes))]
       (or
         (.getContext canvas-element "experimental-webgl" opts)
         (.getContext canvas-element "webgl" opts)))))

(defn get-context-attributes
  "Returns the actual context parameters for a created context. The returned map has the following form:

    {:alpha
     :depth
     :stencil
     :antialias
     :premultiplied-apha
     :preserve-drawing-buffer
     :prefer-low-power-to-high-performance
     :fail-if-major-performance-caveat}

This function is helpful for testing if the requested parameters were satisfied."
  [gl-context]
  (let [js-obj (.getContextAttributes gl-context)]
    {:alpha (.-alpha js-obj),
     :depth (.-depth js-obj),
     :stencil (.-stencil js-obj),
     :antialias (.-antialias js-obj),
     :premultiplied-alpha (.-premultipliedAlpha js-obj),
     :preserve-drawing-buffer (.-preserveDrawingBuffer js-obj),
     :prefer-low-power-to-high-performance (.-preferLowPowerToHighPerformance js-obj),
     :fail-if-major-performance-caveat  (.-failIfMajorPerformanceCaveat js-obj)}))

(defn get-canvas
  "Returns the canvas element from the given WebGL context."
  [gl-context]
  (.-canvas gl-context))

;; The following two functions will generally return the canvas element dimensions, then they are helpful for when the canvas is resized.

;; Note that there is no guarantee that the drawing buffer will have the same size of the canvas, in which case the returned values will correspond to the actual drawing buffer dimension.

;; See [The Drawing Buffer](http://www.khronos.org/registry/webgl/specs/1.0/#THE_DRAWING_BUFFER) for details.
(defn get-drawing-buffer-width
  "Returns the buffer current width."
  [gl-context]
  (.-drawingBufferWidth gl-context))

(defn get-drawing-buffer-height
  "Returns the buffer current height."
  [gl-context]
  (.-drawingBufferHeight gl-context))

(defn get-viewport
  "Returns the current viewport for a given `gl-context` as a map with the form:

  {:x,
    :y,
    :width,
    :height}"
  [gl-context]
  (let [[x y w h] (.apply js/Array [] (.getParameter gl-context parameter-name/viewport))] ;; TODO: Is there any other way to access typed array values?
    {:x      x,
     :y      y,
     :width  w,
     :height h}))

(defn is-context-lost?
  "Returns whether the context was lost.

  See [The Context Lost Event](http://www.khronos.org/registry/webgl/specs/1.0/#CONTEXT_LOST)"
  [gl-context]
  (.isContextLost gl-context))

(defn get-supported-extensions
  "Returns a string sequence containing the name for each supported extension"
  [gl-context]
  (lazy-seq (.getSupportedExtensions gl-context)))

(defn get-extension
  "Returns the object for the requested extension. The returned value is not wrapped in any ClojureScript construct."
  [gl-context extension-name]
  (.getExtension gl-context extension-name))
