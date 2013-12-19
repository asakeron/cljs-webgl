(ns cljs-webgl.buffers
  (:require-macros [cljs.core.match.macros :refer [match]])
  (:require [cljs.core.match]
            [cljs-webgl.typed-arrays :as ta]
            [cljs-webgl.constants :as constants]
            [cljs-webgl.shaders :as shaders]))

(defn create-buffer
  "Creates a new buffer with initialized `data`. 

  `data` must be a typed-array

  `target` may be `array-buffer` or `element-array-buffer`

  `usage` may be `static-draw` or `dynamic-draw`"
  [gl-context data target usage]
  (let [buffer (.createBuffer gl-context)]
    (.bindBuffer gl-context target buffer)
    (.bufferData gl-context target data usage)
    buffer))

(defn clear-color-buffer
  [gl-context red green blue alpha]
  (.clearColor gl-context red green blue alpha)
  (.clear gl-context constants/color-buffer-bit))

(defn draw!
  [gl-context shader vertex-array  uniforms element-array]
  (let
      [bool->float (fn [val] (if val 1.0 0.0))
       set-uniform (fn [{name :name
                         type :type
                         values :values
                         transpose :transpose}]
                     (let [uniform-location (shaders/get-uniform-location gl-context shader name)]
                       (match [type]
                              [:bool] (.uniform1fv gl-context 
                                                   uniform-location 
                                                   (ta/float32 (map bool->float values)))
                              [:bvec2] (.uniform2fv gl-context
                                                    uniform-location
                                                    (ta/float32 (map bool->float values)))
                              [:bvec3] (.uniform3fv gl-context
                                                    uniform-location
                                                    (ta/float32 (map bool->float values)))
                              [:bvec4] (.uniform4fv gl-context
                                                    uniform-location
                                                    (ta/float (map bool->float values)))
                              [:float] (.uniform1fv gl-context
                                                    uniform-location
                                                    (ta/float32 values))
                              [:vec2] (.uniform2fv gl-context
                                                   uniform-location
                                                   (ta/float32 values))
                              [:vec3] (.uniform3fv gl-context
                                                   uniform-location
                                                   (ta/float32 values))
                              [:vec4] (.uniform4fv gl-context
                                                   uniform-location
                                                   (ta/float32 values))
                              [:int] (.uniform1iv gl-context
                                                  uniform-location
                                                  (ta/int32 values))
                              [:ivec2] (.uniform2iv gl-context
                                                    uniform-location
                                                    (ta/int32 values))
                              [:ivec3] (.uniform3iv gl-context
                                                    uniform-location
                                                    (ta/int32 values))
                              [:ivec4] (.uniform4iv gl-context
                                                    uniform-location
                                                    (ta/int32 values))
                              [:mat2] (.uniformMatrix2fv gl-context
                                                         uniform-location
                                                         transpose
                                                         (ta/float32 values))
                              [:mat3] (.uniformMatrix3fv gl-context
                                                         uniform-location
                                                         transpose
                                                         (ta/float32 values))
                              [:mat4] (.uniformMatrix4fv gl-context
                                                         uniform-location
                                                         transpose
                                                         (ta/float32 values))
                              :else nil)))]
    (.useProgram gl-context shader)
    (dorun (map #(set-uniform %) uniforms))
    (.bindBuffer gl-context constants/array-buffer (:buffer vertex-array))
    (.enableVertexAttribArray gl-context (:attrib-array vertex-array))
    (.vertexAttribPointer gl-context (:attrib-array vertex-array) (:components-per-vertex vertex-array)
                          (:type vertex-array) (:normalized? vertex-array) (:stride vertex-array) (:offset vertex-array))
    (if (nil? element-array)
      (.drawArrays gl-context (:mode vertex-array) (:first vertex-array) (:count vertex-array))
      ((fn []
         (.bindBuffer gl-context constants/element-array-buffer (:buffer element-array)) 
         (.drawElements gl-context (:mode vertex-array) (:count element-array) (:type element-array) (:offset element-array)))))))
