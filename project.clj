(defproject cljs-webgl "0.1.0"
  :plugins [[lein-cljsbuild "0.3.2"]]
  :cljsbuild {
              :test-commands {
                              "unit" ["slimerjs"
                                      "slimer/unit-test.js"
                                      "slimer/unit-test.html"]}
              :builds {
                       :debug
                       {:source-paths ["src-cljs"]
                        :compiler {:output-to "target/cljs-webgl.js"}}
                       
                       :release
                       {:source-paths ["src-cljs"]
                        :compiler {:output-to "target/cljs-webgl.min.js"
                                   :optimizations :advanced
                                   :pretty-print false}}
                       :test
                       {:source-paths ["test-cljs" "src-cljs"]
                        :compiler {:output-to "slimer/tests.js"}}}})
