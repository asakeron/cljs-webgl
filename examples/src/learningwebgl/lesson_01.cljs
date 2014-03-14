(ns learningwebgl.lesson-01
  (:require
    [cljs-webgl.buffers :refer [create-buffer clear-color-buffer draw!]]
    [cljs-webgl.context :refer [get-context]]
    [cljs-webgl.shaders :refer [get-shader create-program]]
    [cljs-webgl.constants :as const]
    [cljs-webgl.typed-arrays :as ta]))

(enable-console-print!)

(defn init-gl [canvas]
  (let [gl (get-context canvas)]
    (when-not gl
      (throw (js/Error. "Could not initialize WebGL")))

    (set! (.-viewportWidth gl) (.-width canvas))
    (set! (.-viewportHeight gl) (.-height canvas))
    gl))

(defn init-shaders [gl]
  (let [fragment-shader (get-shader gl "shader-fs")
        vertex-shader (get-shader gl "shader-vs")]
    (create-program gl fragment-shader vertex-shader)))

(defn start []
  (try
    (let [canvas      (.getElementById js/document "canvas")
          gl          (init-gl canvas)
          shader-prog (init-shaders gl)
          triangle-vertex-buffer
                      (create-buffer gl
                        (ta/float32 [ 0.0  1.0  0.0
                                     -1.0 -1.0  0.0
                                      1.0 -1.0  0.0 ])
                        const/array-buffer
                        const/static-draw)
          square-vertex-buffer
                      (create-buffer gl
                        (ta/float32 [ 1.0,  1.0,  0.0,
                                     -1.0,  1.0,  0.0,
                                      1.0, -1.0,  0.0,
                                     -1.0, -1.0,  0.0])
                        const/array-buffer
                        const/static-draw)]

      (clear-color-buffer gl 0.0 0.0 0.0 1.0)
      (.enable gl const/depth-test)

      (draw!
        gl
        shader-prog

        ; draw-mode
        const/triangles

        0 ; first
        3 ; count

        ; attributes
        []

        ; uniforms
        []

        ;element-array
        {}))

  (catch js/Error e
    (js/alert e))))

;        shaderProgram.vertexPositionAttribute = gl.getAttribLocation(shaderProgram, "aVertexPosition");
;        gl.enableVertexAttribArray(shaderProgram.vertexPositionAttribute)
;
;        shaderProgram.pMatrixUniform = gl.getUniformLocation(shaderProgram, "uPMatrix");
;        shaderProgram.mvMatrixUniform = gl.getUniformLocation(shaderProgram, "uMVMatrix");
;
;        var mvMatrix = mat4.create();
;        var pMatrix = mat4.create();
;
;        gl.uniformMatrix4fv(shaderProgram.pMatrixUniform, false, pMatrix);
;        gl.uniformMatrix4fv(shaderProgram.mvMatrixUniform, false, mvMatrix);
;
;
;    function drawScene() {
;        gl.viewport(0, 0, gl.viewportWidth, gl.viewportHeight);
;        gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
;
;        mat4.perspective(45, gl.viewportWidth / gl.viewportHeight, 0.1, 100.0, pMatrix);
;
;        mat4.identity(mvMatrix);
;
;        mat4.translate(mvMatrix, [-1.5, 0.0, -7.0]);
;        gl.bindBuffer(gl.ARRAY_BUFFER, triangleVertexPositionBuffer);
;        gl.vertexAttribPointer(shaderProgram.vertexPositionAttribute, triangleVertexPositionBuffer.itemSize, gl.FLOAT, false, 0, 0);
;        setMatrixUniforms();
;        gl.drawArrays(gl.TRIANGLES, 0, triangleVertexPositionBuffer.numItems);
;
;
;        mat4.translate(mvMatrix, [3.0, 0.0, 0.0]);
;        gl.bindBuffer(gl.ARRAY_BUFFER, squareVertexPositionBuffer);
;        gl.vertexAttribPointer(shaderProgram.vertexPositionAttribute, squareVertexPositionBuffer.itemSize, gl.FLOAT, false, 0, 0);
;        setMatrixUniforms();
;        gl.drawArrays(gl.TRIANGLE_STRIP, 0, squareVertexPositionBuffer.numItems);
;    }
