(ns cljswebgl.gl.shaders)

(defn create-shader
  [gl-context type source]
  (let [shader (.createShader gl-context type)]
    (.shaderSource gl-context shader source)
    (.compileShader gl-context shader)
    shader))

(defn create-program
  [gl-context shaders]
  (let [program (.createProgram gl-context)]
    (dorun (map (fn [shader] (.attachShader gl-context program shader)) shaders))
    program))

(defn get-attached-shaders
  [gl-context program]
  (list (.getAttachedShaders gl-context program)))

(defn get-shader-source
  [gl-context shader]
  (.getShaderSource gl-context shader))

(defn get-shader-info-log
  [gl-context shader]
  (.getShaderInfoLog gl-context shader))

(defn get-shader-parameter
  "Parameter may be the constants shader-type, compile-status and delete-status"
  [gl-context shader parameter]
  (.getShaderParameter gl-context shader parameter))

(defn get-shader-precision-format
  "shader-type may be fragment-shader or vertex-shader. precision type may be low-float, medium-float, high-float, low-int, medium-int or high-int"
  [gl-context shader-type precision-type]
  (let [js-obj (.getShaderPrecisionFormat gl-context shader-type precision-type)]
    {:range-min (.-rangeMin js-obj)
     :range-max (.-rangeMax js-obj)
     :precision (.-precision js-obj)}))

(defn is-shader?
  [gl-context shader]
  (.isShader gl-context shader))
