(ns cljs-webgl.misc)

(defn is-capability-enabled?
  "Tests whether a particular capability is enabled or not.

   `capability` is a single of capability to test.

   The valid values for capability are: `cljs-webgl.constants/blend`,
   `cljs-webgl.constants/cull-face`, `cljs-webgl.constants/depth-test`, `cljs-webgl.constants/dither`,
   `cljs-webgl.constants/polygon-offset-fill`, `cljs-webgl.constants/sample-alpha-to-coverage`,
   `cljs-webgl.constants/sample-coverage`, `cljs-webgl.constants/scissor-test`,
   `cljs-webgl.constants/stencil-test`

   Relevant OpenGL ES reference pages:

   [glIsEnabled](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glIsEnabled.xml)"
  [gl-context capability]
  (.isEnabled gl-context capability))
