(ns cljs-webgl.typed-arrays)

(defn int8
  [collection]
  (js/Int8Array. (clj->js collection)))

(defn unsigned-int8
  [collection]
  (js/Uint8Array. (clj->js collection)))

(defn unsigned-int8-clamped
  [collection]
  (js/Uint8ClampedArray. (clj->js collection)))

(defn int16
  [collection]
  (js/Int16Array. (clj->js collection)))

(defn unsigned-int16
  [collection]
  (js/Uint16Array. (clj->js collection)))

(defn int32
  [collection]
  (js/Int32Array. (clj->js collection)))

(defn unsigned-int32
  [collection]
  (js/Uint32Array. (clj->js collection)))

(defn float32
  [collection]
  (js/Float32Array. (clj->js collection)))

(defn float64
  [collection]
  (js/Float64Array. (clj->js collection)))
