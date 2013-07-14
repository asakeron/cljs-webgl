(ns cljs-webgl.buffers)

(defn create-buffer
  ;; target may be gl-array-buffer or gl-element-array-buffer
  [gl-context data target usage]
  (let [buffer (.createBuffer gl-context)]
    (.bindBuffer gl-context target buffer)
    (.bufferData gl-context target data usage)
    buffer))

(defn draw-arrays
  [gl-context shader buffer target vertex-attrib-array draw-type vertex-type num-vertices uniforms]
  (let [set-uniform 
        (fn [uniform]
        
          (defmulti set-specific-uniform (fn [uniform-location values] 
                                             [(.-length values) (type values)]))
            
          (defmethod set-specific-uniform [1 js/Float32Array] [uniform-location values]
            (.uniform1fv gl-context uniform-location values))
          
          (defmethod set-specific-uniform [2 js/Float32Array] [uniform-location values]
            (.uniform2fv gl-context uniform-location values))
            
          (defmethod set-specific-uniform [3 js/Float32Array] [uniform-location values]
              (.uniform3fv gl-context uniform-location values))
          
          (defmethod set-specific-uniform [4 js/Float32Array] [uniform-location values]
            (.uniform4fv gl-context uniform-location values))

          (defmethod set-specific-uniform [1 js/Int32Array] [uniform-location values]
            (.uniform1iv gl-context uniform-location values))
            
          (defmethod set-specific-uniform [2 js/Int32Array] [uniform-location values]
            (.uniform2iv gl-context uniform-location values))
          
          (defmethod set-specific-uniform [3 js/Int32Array] [uniform-location values]
            (.uniform3iv gl-context uniform-location values))
          
          (defmethod set-specific-uniform [4 js/Int32Array] [uniform-location values]
            (.uniform4iv gl-context uniform-location values))
          
          (set-specific-uniform (:location uniform) (:values uniform)))]
    (.useProgram gl-context shader)
    (dorun (map #(set-uniform %) uniforms))
    (.bindBuffer gl-context target buffer)
    (.enableVertexAttribArray gl-context vertex-attrib-array)
    (.vertexAttribPointer gl-context 0 2 vertex-type false 0 0)
    (.drawArrays gl-context draw-type 0 num-vertices)))
