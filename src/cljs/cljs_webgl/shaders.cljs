(ns cljs-webgl.shaders
  (:require [cljs-webgl.constants.shader :as shader]
            [cljs-webgl.constants.shader-source :as shader-source]))

(defn get-program-parameter
  "Returns the value of a given `parameter` in a `program` object.

  Valid values for `parameter` are `cljs-webgl.constants.shader/validate-status`, `cljs-webgl.constants.shader/link-status` and `cljs-webgl.constants.shader/delete-status`.

  Relevant OpenGL ES reference pages:

  * [glGetProgramiv (similar to getProgramParameter)](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glGetProgramiv.xml)"
  [gl-context program parameter]
  (.getProgramParameter gl-context program parameter))

(defn get-attached-shaders
  "Returns a lazy sequence of shader objects attached to a given shader `program`.

  Relevant OpenGL ES reference pages:

  * [glGetAttachedShaders](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glGetAttachedShaders.xml)"
  [gl-context program]
  (lazy-seq (.getAttachedShaders gl-context program)))

(defn get-shader-source
  "Returns the source code for a given `shader` object.

  Relevant OpenGL ES reference pages:

  * [glGetShaderSource](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glGetShaderSource.xml)"
  [gl-context shader]
  (.getShaderSource gl-context shader))

(defn get-shader-info-log
  "Returns the information log for a given `shader` object. Useful for checking for compile errors.

  Relevant OpenGL ES reference pages:

  * [glGetShaderInfoLog](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glGetShaderInfoLog.xml)"
  [gl-context shader]
  (.getShaderInfoLog gl-context shader))

(defn get-shader-parameter
  "Returns the value of a given `parameter` in a `shader` object.

  Valid values for `parameter` are `cljs-webgl.constants.shader/shader-type`, `cljs-webgl.constants.shader/compile-status` and `cljs-webgl.constants.shader/delete-status`.

  Relevant OpenGL ES reference pages:

  * [glGetShaderiv(similar to getShaderParameter)](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glGetShaderiv.xml)"
  [gl-context shader parameter]
  (.getShaderParameter gl-context shader parameter))

(defn get-shader-precision-format
  "Returns a map describing the range and precision for the specified `shader-type` and `precision-type`. The returned map
  has the following form:

    {:range-min
     :range-max
     :precision}

  Valid values for `shader-type` are `cljs-webgl.constants.shader/fragment-shader` and `cljs-webgl.constants.shader/vertex-shader`.

  Valid values for `precision type` are `cljs-webgl.constants.shader-precision-specified-types/low-float`,
  `cljs-webgl.constants.shader-precision-specified-types/medium-float`,
  `cljs-webgl.constants.shader-precision-specified-types/high-float`, `cljs-webgl.constants.shader-precision-specified-types/low-int`,
  `cljs-webgl.constants.shader-precision-specified-types/medium-int` and `cljs-webgl.constants.shader-precision-specified-types/high-int`

  Relevant OpenGL ES reference pages:

  * [glGetShaderPrecisionFormat](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glGetShaderPrecisionFormat.xml)"
  [gl-context shader-type precision-type]
  (let [js-obj (.getShaderPrecisionFormat gl-context shader-type precision-type)]
    {:range-min (.-rangeMin js-obj)
     :range-max (.-rangeMax js-obj)
     :precision (.-precision js-obj)}))

(defn get-attrib-location
  "Returns the attribute - specified by it's name - location in a given `shader-program`.

  Relevant OpenGL ES reference pages:

  * [glGetAttribLocation](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glGetAttribLocation.xml)"
  [gl-context shader-program attrib-name]
  (.getAttribLocation gl-context shader-program attrib-name))

(defn get-uniform-location
  "Returns the uniform - specified by it's name - location in a given `shader-program`.

  Relevant OpenGL ES reference pages:

  * [glGetUniformLocation](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glGetUniformLocation.xml)"
  [gl-context shader-program uniform-name]
  (.getUniformLocation gl-context shader-program uniform-name))

(defn is-shader?
  "Returns whether a given shader object is valid.

  Relevant OpenGL ES reference pages:

  * [glIsShader](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glIsShader.xml)"
  [gl-context shader]
  (.isShader gl-context shader))

(defn is-program?
  "Returns whether a given shader program is valid.

  Relevant OpenGL ES reference pages:

  * [glIsProgram](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glIsProgram.xml)"
  [gl-context shader-program]
  (.isProgram gl-context shader-program))

(defn create-shader
  "Returns a compiled vertex or fragment shader object (specified by the `type` parameter)
   for a given `source`. If the shader cannot be compiled successfully, an error is thrown.

  The valid values for `type` are `cljs-webgl.constants.shader/vertex-shader` and `cljs-webgl.constants.shader/fragment-shader`.

  Relevant OpenGL ES reference pages:

  * [glCreateShader](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glCreateShader.xml)
  * [glShaderSource](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glShaderSource.xml)
  * [glCompileShader](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glCompileShader.xml)"
  [gl-context type source]
  (let [shader (.createShader gl-context type)]
    (.shaderSource gl-context shader source)
    (.compileShader gl-context shader)

    (when-not (get-shader-parameter gl-context shader shader-source/compile-status)
      (throw (js/Error. (get-shader-info-log gl-context shader))))

    shader))


(defn ^:private text-content
  "Grabs the text content of the element's children"
  [element]
  (loop [child (.-firstChild element)
         text  ""]
    (if (nil? child)
      text
      (recur
        (.-nextSibling child)
        (str
          text
          (when (= (.-nodeType child) 3) ; 3=TEXT_NODE
            (.-textContent child)))))))

(def ^:private mime-type
  "Mapping of mime/type to relevant GL constant"
  {"x-shader/x-fragment" shader/fragment-shader
   "x-shader/x-vertex"   shader/vertex-shader})

(defn get-shader
  "Returns a compiled vertext or fragment shader, loaded from the script-id"
  [gl-context script-id]
  (when-let [script (.getElementById js/document script-id)]
    (create-shader
      gl-context
      (mime-type (.-type script))
      (text-content script))))

(defn create-program
  "Returns a linked shader program composed of the compiled shader objects
   specified by the `shaders` parameter. Throws an error if the program was
   not linked successfully.

  Relevant OpenGL ES reference pages:

  * [glCreateProgram](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glCreateProgram.xml)
  * [glAttachShader](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glAttachShader.xml)
  * [glLinkProgram](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glLinkProgram.xml)"
  [gl-context & shaders]
  (let [program (.createProgram gl-context)]

    (doseq [shader shaders]
      (.attachShader gl-context program shader))

    (.linkProgram gl-context program)

    (when-not (get-program-parameter gl-context program shader/link-status)
      (throw (js/Error. "Could not initialize shaders")))

    (.useProgram gl-context program)
    program))

