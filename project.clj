(defproject cljs-webgl "0.1.4-SNAPSHOT"
  :description "WebGL binding to ClojureScript"
  :url "https://github.com/Asakeron/cljs-webgl"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojurescript "0.0-2173"]
                 [org.clojure/core.match "0.2.1"]]
  :source-paths ["src/cljs"]
  :plugins [[lein-cljsbuild "1.0.2"]
            [lein-marginalia "0.7.1"]]
  :cljsbuild {
    :builds {
      :examples {
        :source-paths ["src/cljs" "examples/src"]
        :incremental? true
        :compiler {
          :output-to "target/example.js"
          :source-map "target/example.map"
          :foreign-libs [{:file "resources/js/gl-matrix-min.js" :provides ["mat4"]}]
          :static-fns true
          :pretty-print true }}}})
