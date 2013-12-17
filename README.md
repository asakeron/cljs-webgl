cljs-webgl
----
WebGL binding to ClojureScript.

Goal
----
Create a library that allows a more convinient functional style for creating WebGL applications in ClojureScript, by means of hiding mutability where possible and wrapping every Javascript value in ClojureScript constructs.

Example
----

```clojure
(ns cljs-webgl-triangle.core
  (:require [cljs-webgl.context :as context]
            [cljs-webgl.shaders :as shaders]
            [cljs-webgl.constants :as constants]
            [cljs-webgl.buffers :as buffers]
            [cljs-webgl.typed-arrays :as ta]))

(let
    [element (.getElementById js/document "canvas-demo")
     gl (context/get-context element)
     fragment-shader (shaders/create-shader gl
                                            constants/fragment-shader
                                            (.-innerHTML (.getElementById
					    		  js/document
							  "fragment-shader")))
     vertex-shader (shaders/create-shader gl
                                          constants/vertex-shader
                                          (.-innerHTML (.getElementById
							js/document
							"vertex-shader")))
     shader-program (shaders/create-program gl [vertex-shader
                                                fragment-shader])
     triangle-buffer (buffers/create-buffer gl
                                            (ta/float32 [0.0 1.0
                                                         -1.0 -1.0
                                                         1.0 -1.0])
                                            constants/array-buffer
                                            constants/static-draw)]
  (buffers/draw-arrays gl
                       shader-program
                       triangle-buffer
                       constants/array-buffer
                       (shaders/get-attrib-location gl
                                                    shader-program
                                                    "vertex_position")
                       constants/triangles
                       constants/float
                       0
                       2
                       false
                       0
                       0
                       3
                       [{:name "color" :type :vec4 :values [1.0 0.0 0.0 1.0]}]))
```

Dependency information
----

[Leiningen](http://github.com/technomancy/leiningen/) dependency information:

```
[cljs-webgl "0.1.3-SNAPSHOT"]
```

Documentation
----
For generating the documentation, run `lein marg src-cljs`.