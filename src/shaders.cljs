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
