cljs-webgl
----
WebGL binding to ClojureScript.

Goal
----
Create a library that allows a more convinient functional style for creating WebGL applications in ClojureScript, by means of hiding mutability where possible and wrapping every Javascript value in ClojureScript constructs.

Example
----
The program below can be run by first building:

    $ lein cljsbuild once

And then opening the `examples/index.html` page in a webGL capable browser.

```clojure
(ns basic-opengl-program.core
  (:require [cljs-webgl.context :as context]
            [cljs-webgl.shaders :as shaders]
            [cljs-webgl.constants.draw-mode :as draw-mode]
            [cljs-webgl.constants.data-type :as data-type]
            [cljs-webgl.constants.buffer-object :as buffer-object]
            [cljs-webgl.constants.shader :as shader]
            [cljs-webgl.buffers :as buffers]
            [cljs-webgl.typed-arrays :as ta]))

(def vertex-shader-source
  "attribute vec3 vertex_position;
   void main() {
     gl_Position = vec4(vertex_position, 1);
   }")

(def fragment-shader-source
  "uniform int frame;
   void main() {
     gl_FragColor.r = sin(float(frame) * 0.05) / 2.0 + 0.5;
     gl_FragColor.g = sin(float(frame) * 0.1) / 2.0 + 0.5;
     gl_FragColor.b = sin(float(frame) * 0.02) / 2.0 + 0.5;
     gl_FragColor.a = 1.0;
   }")

(defn start []
  (let
    [gl (context/get-context (.getElementById js/document "canvas"))
     shader (shaders/create-program gl
              (shaders/create-shader gl shader/vertex-shader vertex-shader-source)
              (shaders/create-shader gl shader/fragment-shader fragment-shader-source))
     vertex-buffer (buffers/create-buffer gl (ta/float32 [1.0 1.0 0.0
                                                          -1.0 1.0 0.0
                                                          1.0 -1.0 0.0])
                                          buffer-object/array-buffer
                                          buffer-object/static-draw)
     element-buffer (buffers/create-buffer gl (ta/unsigned-int16 [0 1 2])
                                           buffer-object/element-array-buffer
                                           buffer-object/static-draw)
     draw (fn [frame continue]
            (-> gl
                (buffers/clear-color-buffer 0 0 0 1)
                (buffers/draw! :shader shader
                               :draw-mode draw-mode/triangles
                               :count 3

                               :attributes
                               [{:buffer vertex-buffer
                                 :location (shaders/get-attrib-location gl shader "vertex_position")
                                 :components-per-vertex 3
                                 :type data-type/float}]

                               :uniforms
                               [{:name "frame" :type :int :values (ta/int32 [frame])}]

                               :element-array
                               {:buffer element-buffer
                                :count 3
                                :type data-type/unsigned-short
                                :offset 0}))

            (.requestAnimationFrame js/window (fn [time-elapsed] (continue (inc frame) continue))))]
  (.requestAnimationFrame js/window (fn [time-elapsed] (draw 0 draw)))))

```

More [working examples](http://asakeron.github.io/cljs-webgl/examples/learningwebgl/lesson_01.html) 
(clojurescript implementations of [Learning WebGL](http://learningwebgl.com/blog/?page_id=1217))
can be found in the examples directory

Dependency information
----

[Leiningen](http://github.com/technomancy/leiningen/) dependency information:

```
[cljs-webgl "0.1.4-SNAPSHOT"]
```

Documentation
----
See the [API Documentation](http://asakeron.github.io/cljs-webgl/doc/api).

To generate Marginalia documentation locally, run `lein marg src/cljs`.
