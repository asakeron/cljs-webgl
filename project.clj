(defproject cljs-webgl "0.1.0"
	:plugins [[lein-cljsbuild "0.3.2"]]
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
