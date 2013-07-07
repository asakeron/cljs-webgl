(ns cljs-webgl.buffer)

(defn create-buffer
  ;; target may be gl-array-buffer or gl-element-array-buffer
  [gl-context data target usage]
  (let [buffer (.createBuffer gl-context)]
    (.bindBuffer gl-context target buffer)
    (.bufferData gl-context target data usage)
    buffer))
