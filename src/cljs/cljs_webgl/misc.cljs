(ns cljs-webgl.misc)

(defn is-capability-enabled?
  "Tests whether a particular capability is enabled or not.

   `capability` is a single of capability to test.

   The valid values for capability are: `cljs-webgl.constants.capability/blend`,
   `cljs-webgl.constants.capability/cull-face`, `cljs-webgl.constants.capability/depth-test`,
   `cljs-webgl.constants.capability/dither`, `cljs-webgl.constants.capability/polygon-offset-fill`,
   `cljs-webgl.constants.capability/sample-alpha-to-coverage`, `cljs-webgl.constants.capability/sample-coverage`,
   `cljs-webgl.constants.capability/scissor-test`, `cljs-webgl.constants.capability/stencil-test`

   Relevant OpenGL ES reference pages:

   [glIsEnabled](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glIsEnabled.xml)"
  [gl-context capability]
  (.isEnabled gl-context capability))
