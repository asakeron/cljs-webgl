(ns cljs-webgl.typed-arrays)

(defn int8
  "Creates a native Int8Array for a given `collection`."
  [collection]
  (js/Int8Array. (clj->js collection)))

(defn unsigned-int8
  "Creates a native Uint8Array for a given `collection`."
  [collection]
  (js/Uint8Array. (clj->js collection)))

(defn unsigned-int8-clamped
  "Creates a native Uint8ClampedArray for a given `collection`."
  [collection]
  (js/Uint8ClampedArray. (clj->js collection)))

(defn int16
  "Creates a native Int16Array for a given `collection`."
  [collection]
  (js/Int16Array. (clj->js collection)))

(defn unsigned-int16
  "Creates a native Uint16Array for a given `collection`."
  [collection]
  (js/Uint16Array. (clj->js collection)))

(defn int32
  "Creates a native Int32Array for a given `collection`."
  [collection]
  (js/Int32Array. (clj->js collection)))

(defn unsigned-int32
  "Creates a native Uint32Array for a given `collection`."
  [collection]
  (js/Uint32Array. (clj->js collection)))

(defn float32
  "Creates a native Float32Array for a given `collection`."
  [collection]
  (js/Float32Array. (clj->js collection)))

(defn float64
  "Creates a native Float64Array for a given `collection`."
  [collection]
  (js/Float64Array. (clj->js collection)))

(defn typed-array?
  "Tests whether a given `value` is a typed array."
  [value]
  (let [value-type (type value)]
    (or 
     (= value-type js/Int8Array)
     (= value-type js/Uint8Array)
     (= value-type js/Uint8ClampedArray)
     (= value-type js/Int16Array)
     (= value-type js/Uint16Array)
     (= value-type js/Int32Array)
     (= value-type js/Uint32Array)
     (= value-type js/Float32Array)
     (= value-type js/Float64Array))))
