(defproject cljs-webgl "0.1.6-SNAPSHOT"
  :description "WebGL binding to ClojureScript"
  :url "https://github.com/Asakeron/cljs-webgl"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.clojure/clojurescript "1.10.866"]]
  :source-paths ["src/cljs"]
  :plugins [[lein-cljsbuild "1.1.8"]
            [lein-marginalia "0.9.1"]]
  :cljsbuild {
    :builds {
      :src {
        :jar true
        :source-paths ["src/cljs"]
        :incremental? true
        :foreign-libs [
            {:file "resources/js/gl-matrix-min.js" :provides ["mat4","mat3","vec3"]}
            {:file "resources/js/webgl-utils.js" :provides ["WebGLUtils"]}]}
      :examples {
        :source-paths ["src/cljs" "examples/src"]
        :incremental? true
        :compiler {
          :output-to "target/example.js"
          :source-map "target/example.map"
          :foreign-libs [
            {:file "resources/js/gl-matrix-min.js" :provides ["mat4","mat3","vec3"]}
            {:file "resources/js/webgl-utils.js" :provides ["WebGLUtils"]}]
          :static-fns true
          :closure-warnings {
            :externs-validation :off
            :non-standard-jsdoc :off}
          :pretty-print false
          :optimizations :simple}}}})
