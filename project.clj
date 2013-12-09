(defproject cljs-webgl "0.1.3-SNAPSHOT"
  :description "WebGL binding to ClojureScript"
  :url "https://github.com/Asakeron/cljs-webgl"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojurescript "0.0-2080"]]
  :source-paths ["src/cljs"]
  :plugins [[lein-cljsbuild "1.0.1-SNAPSHOT"]
            [lein-marginalia "0.7.1"]]
  :cljsbuild {:builds
              [{:source-paths ["src/cljs"]
                :compiler {:output-to "target/cljs-webgl.min.js"
                           :source-map "target/cljs-webgl.min.js.map"
                           :optimizations :advanced
                           :pretty-print false}}]})
