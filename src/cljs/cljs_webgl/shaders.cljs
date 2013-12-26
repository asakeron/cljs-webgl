(ns cljs-webgl.shaders)

(defn create-shader
  "Returns a compiled vertex or fragment shader - specified by the `type` parameter -, object for a given `source`.

  The valid values for `type` are `cljs-webgl.constants/vertex-shader` and `cljs-webgl.constants/fragment-shader`.

  Relevant OpenGL ES reference pages:
  
  * [glCreateShader](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glCreateShader.xml)
  * [glShaderSource](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glShaderSource.xml)
  * [glCompileShader](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glCompileShader.xml)"
  [gl-context type source]
  (let [shader (.createShader gl-context type)]
    (.shaderSource gl-context shader source)
    (.compileShader gl-context shader)
    shader))

(defn create-program
  "Returns a linked shader program composed of the compiled shader objects specified by the `shaders` parameter.

  Relevant OpenGL ES reference pages:
  
  * [glCreateProgram](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glCreateProgram.xml)
  * [glAttachShader](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glAttachShader.xml)
  * [glLinkProgram](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glLinkProgram.xml)"
  [gl-context shaders]
  (let [program (.createProgram gl-context)]
    (dorun (map (fn [shader] (.attachShader gl-context program shader)) shaders))
    (.linkProgram gl-context program)
    program))

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
  
  Valid values for `parameter` are `cljs-webgl.constants/shader-type`, `cljs-webgl.constants/compile-status` and `cljs-webgl.constants/delete-status`.
  
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

  Valid values for `shader-type` are `cljs-webgl.constants/fragment-shader` and `cljs-webgl.constants/vertex-shader`. 

  Valid values for `precision type` are `cljs-webgl.constants/low-float`, `cljs-webgl.constants/medium-float`,
  `cljs-webgl.constants/high-float`, `cljs-webgl.constants/low-int`, `cljs-webgl.constants/medium-int`
  and `cljs-webgl.constants/high-int`

  Relevant OpenGL ES reference pages:

  * [glGetShaderPrecisionFormat](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glGetShaderPrecisionFormat.xml)"
  [gl-context shader-type precision-type]
  (let [js-obj (.getShaderPrecisionFormat gl-context shader-type precision-type)]
    {:range-min (.-rangeMin js-obj)
     :range-max (.-rangeMax js-obj)
     :precision (.-precision js-obj)}))

(defn get-attrib-location
  "Returns the attribute - specified by it's name` - location in a given `shader-program`.

  Relevant OpenGL ES reference pages:

  * [glGetAttribLocation](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glGetAttribLocation.xml)"
  [gl-context shader-program attrib-name]
  (.getAttribLocation gl-context shader-program attrib-name))

(defn get-uniform-location
  "Returns the uniform - specified by it's name` - location in a given `shader-program`.

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

  * [glIsProgram] (http://www.khronos.org/opengles/sdk/docs/man/xhtml/glIsProgram.xml)"
  [gl-context shader-program]
  (.isProgram gl-context shader-program))
