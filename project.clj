(defproject cljs-webgl "0.1.0-SNAPSHOT"
  :description "WebGL binding to ClojureScript"
  :url "https://github.com/Asakeron/cljs-webgl"
  :license "zlib"
  :plugins [[lein-cljsbuild "0.3.2"]
            [lein-marginalia "0.7.1"]]
  
  :cljsbuild {
              :builds {
                       
                       :debug
                       {:source-paths ["src-cljs"]
                        :compiler {:output-to "target/cljs-webgl.js"}}
                       
                       :release
                       {:source-paths ["src-cljs"]
                        :compiler {:output-to "target/cljs-webgl.min.js"
                                   :optimizations :advanced
                                   :pretty-print false}}}})
