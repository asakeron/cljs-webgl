(ns cljs-webgl.misc)

(defn capabilities
  "Enables/disables GL capabilities

   `capabilities-map` is a mapping of capabilities->true/false to enable/disable specific GL capabilities.

   The valid values for each capability are: `cljs-webgl.constants/blend`,
   `cljs-webgl.constants/cull-face`, `cljs-webgl.constants/depth-test`, `cljs-webgl.constants/dither`,
   `cljs-webgl.constants/polygon-offset-fill`, `cljs-webgl.constants/sample-alpha-to-coverage`,
   `cljs-webgl.constants/scissor-test`, `cljs-webgl.constants/stensil-test`

   Relevant OpenGL ES reference pages:

   * [glEnable](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glEnable.xml)
   * [glDisable](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glDisable.xml)"
  [gl-context capabilities-map]
  (doseq [[capability enabled?] capabilities-map]
    (if enabled?
      (.enable gl-context capability)
      (.disable gl-context capability)))
  gl-context)


(defn is-enabled?
  "Tests whether a particular capability is enabled or not.

   `capability` is a single of capability to test.

   The valid values for capability are: `cljs-webgl.constants/blend`,
   `cljs-webgl.constants/cull-face`, `cljs-webgl.constants/depth-test`, `cljs-webgl.constants/dither`,
   `cljs-webgl.constants/polygon-offset-fill`, `cljs-webgl.constants/sample-alpha-to-coverage`,
   `cljs-webgl.constants/scissor-test`, `cljs-webgl.constants/stensil-test`

   Relevant OpenGL ES reference pages:

   [glIsEnabled](http://www.khronos.org/opengles/sdk/docs/man/xhtml/glIsEnabled.xml)"
  [gl-context capability]
  (.isEnabled gl-context capability))
