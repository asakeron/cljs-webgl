(defproject cljs-webgl "0.1.0-SNAPSHOT"
  :description "WebGL binding to ClojureScript"
  :url "https://github.com/Asakeron/cljs-webgl"
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo}
  :plugins [[lein-cljsbuild "1.0.1-SNAPSHOT"]]
  :hooks [leiningen.cljsbuild]
  :dependencies [[org.clojure/clojurescript "0.0-2080"]]
  :source-paths ["src-cljs"]
  :cljsbuild {
    :builds {:debug {:source-paths ["src-cljs"]
                     :compiler {:output-to "target/cljs-webgl.js"}}
             :release {:source-paths ["src-cljs"]
                       :compiler {:output-to "target/cljs-webgl.min.js"
                                 :optimizations :advanced
                                 :pretty-print false}}}})
