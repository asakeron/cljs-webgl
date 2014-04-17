(ns cljs-webgl.buffers
  (:require
    [cljs-webgl.context :as context]
    [cljs-webgl.typed-arrays :as ta]
    [cljs-webgl.constants.capability :as capability]
    [cljs-webgl.constants.clear-buffer-mask :as clear-buffer]
    [cljs-webgl.constants.buffer-object :as buffer-object]
    [cljs-webgl.constants.texture-target :as texture-target]
    [cljs-webgl.constants.texture-unit :as texture-unit]
    [cljs-webgl.constants.data-type :as data-type]
    [cljs-webgl.shaders :as shaders]))

(defn create-buffer
  "Creates a new buffer with initialized `data`.

  `data` must be a typed-array

  `target` may be `cljs-webgl.constants.buffer-object/array-buffer` or `cljs-webgl.constants.buffer-object/element-array-buffer`

  `usage` may be `cljs-webgl.constants.buffer-object/static-draw` or `cljs-webgl.constants.buffer-object/dynamic-draw`

  `item-size` [optional] will set the item size as an attribute on the buffer, and the calculate the number of items accordingly.

  Relevant OpenGL ES reference pages:

  * [glGenBuffers(Similar to createBuffer)](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glGenBuffers.xml)
  * [glBindBuffer](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glBindBuffer.xml)
  * [glBufferData](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glBufferData.xml)"
  [gl-context data target usage & [item-size]]
  (let [buffer (.createBuffer gl-context)]
    (.bindBuffer gl-context target buffer)
    (.bufferData gl-context target data usage)
    (when item-size
      (set! (.-itemSize buffer) item-size)
      (set! (.-numItems buffer) (quot (.-length data) item-size)))
    buffer))

(defn clear-color-buffer
  "Clears the color buffer with specified `red`, `green`, `blue` and `alpha` values.

  Relevant OpenGL ES reference pages:

  * [glClearColor](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glClearColor.xml)
  * [glClear](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glClear.xml)"
  [gl-context red green blue alpha]
  (.clearColor gl-context red green blue alpha)
  (.clear gl-context clear-buffer/color-buffer-bit)
  gl-context)

(defn clear-depth-buffer
  "Clears the depth buffer with specified `depth` value.

  Relevant OpenGL ES reference pages:

  * [glClearDepthf](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glClearDepthf.xml)
  * [glClear](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glClear.xml)"
  [gl-context depth]
  (.clearDepth gl-context depth)
  (.clear gl-context clear-buffer/depth-buffer-bit)
  gl-context)

(defn clear-stencil-buffer
  "Clears the stencil buffer with specified `index` value.

  Relevant OpenGL ES reference pages:

  * [glClearStencil](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glClearStencil.xml)
  * [glClear](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glClear.xml)"
  [gl-context index]
  (.clearStencil gl-context index)
  (.clear gl-context clear-buffer/stencil-buffer-bit)
  gl-context)

(defn ^:private set-uniform
  [gl-context shader {:keys [name type values transpose]}]
  (let [uniform-location (shaders/get-uniform-location gl-context shader name)]
    (case type
      :bool   (.uniform1fv gl-context uniform-location values)
      :bvec2  (.uniform2fv gl-context uniform-location values)
      :bvec3  (.uniform3fv gl-context uniform-location values)
      :bvec4  (.uniform4fv gl-context uniform-location values)
      :float  (.uniform1fv gl-context uniform-location values)
      :vec2   (.uniform2fv gl-context uniform-location values)
      :vec3   (.uniform3fv gl-context uniform-location values)
      :vec4   (.uniform4fv gl-context uniform-location values)
      :int    (.uniform1iv gl-context uniform-location values)
      :ivec2  (.uniform2iv gl-context uniform-location values)
      :ivec3  (.uniform3iv gl-context uniform-location values)
      :ivec4  (.uniform4iv gl-context uniform-location values)
      :mat2   (.uniformMatrix2fv gl-context uniform-location transpose values)
      :mat3   (.uniformMatrix3fv gl-context uniform-location transpose values)
      :mat4   (.uniformMatrix4fv gl-context uniform-location transpose values)
      nil)))

(defn ^:private set-attribute
  [gl-context {:keys [buffer location components-per-vertex type normalized? stride offset]}]
  (.bindBuffer
    gl-context
    buffer-object/array-buffer
    buffer)

  (.enableVertexAttribArray
    gl-context
    location)

  (.vertexAttribPointer
    gl-context
    location
    (or components-per-vertex (.-itemSize buffer))
    (or type data-type/float)
    (or normalized? false)
    (or stride 0)
    (or offset 0)))

(defn ^:private set-texture
  [gl-context shader {:keys [texture name texture-unit]}]
  (let [unit (if texture-unit (+ texture-unit/texture0 texture-unit)
                              texture-unit/texture0)
        uniform-index (or texture-unit 0)]

    (.activeTexture
      gl-context
      texture-unit/texture0)

    (.bindTexture
      gl-context
      texture-target/texture-2d
      texture)

    (.uniform1i
      gl-context
      (shaders/get-uniform-location gl-context shader name)
      0)))

(def ^:private default-capabilities
  {capability/blend                    false
   capability/cull-face                false
   capability/depth-test               false
   capability/dither                   true
   capability/polygon-offset-fill      false
   capability/sample-alpha-to-coverage false
   capability/sample-coverage          false
   capability/scissor-test             false
   capability/stencil-test             false})

(defn ^:private set-capability
  "Enables/disables according to `enabled?` a given server-side GL `capability`

   The valid values for `capability` are: `cljs-webgl.constants.capability/blend`,
   `cljs-webgl.constants.capability/cull-face`, `cljs-webgl.constants.capability/depth-test`, `cljs-webgl.constants.capability/dither`,
   `cljs-webgl.constants.capability/polygon-offset-fill`, `cljs-webgl.constants.capability/sample-alpha-to-coverage`,
   `cljs-webgl.constants.capability/sample-coverage`, `cljs-webgl.constants.capability/scissor-test`,
   `cljs-webgl.constants.capability/stencil-test`

   Relevant OpenGL ES reference pages:

   * [glEnable](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glEnable.xml)
   * [glDisable](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glDisable.xml)"
  [gl-context capability enabled?]

  (if enabled?
    (.enable gl-context capability)
    (.disable gl-context capability))
  gl-context)

(defn ^:private set-viewport
  "Sets `gl-context` viewport according to `viewport` which is expected to have the form:

  {:x,
   :y,
   :width,
   :height}

  Relevant OpenGL ES reference pages:

  * [viewport](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glViewport.xml)"
  [gl-context {:keys [x y width height] :as viewport}]
  (.viewport gl-context x y width height))

(defn draw!
  [gl-context & {:keys [shader draw-mode first count attributes
                        uniforms textures element-array capabilities
                        blend-function viewport] :as opts}]

  (set-viewport gl-context (or viewport
                               {:x      0,
                                :y      0,
                                :width  (context/get-drawing-buffer-width gl-context),
                                :height (context/get-drawing-buffer-height gl-context)}))

  (.useProgram gl-context shader)

  (doseq [u uniforms]
    (set-uniform gl-context shader u))

  (doseq [a attributes]
    (set-attribute gl-context a))

  (doseq [t textures]
    (set-texture gl-context shader t))

  (doseq [[capability enabled?] (merge default-capabilities capabilities)]
    (set-capability gl-context capability enabled?))

  (if (nil? element-array)
    (.drawArrays gl-context draw-mode (or first 0) count)
    (do
      (.bindBuffer gl-context buffer-object/element-array-buffer (:buffer element-array))
      (.drawElements gl-context draw-mode count (:type element-array) (:offset element-array))))

  (doseq [a attributes]
    (.disableVertexAttribArray gl-context (:location a)))

  (doseq [[k v] blend-function]
    (.blendFunc gl-context k v))

  gl-context)
