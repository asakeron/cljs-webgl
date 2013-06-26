(defproject cljs-webgl "0.1.0"
	:plugins [[lein-cljsbuild "0.3.2"]]
	:cljsbuild {
		:builds [{
			:source-paths ["src"]
			:compiler {
				:output-to "out/cljs-webgl.min.js"
				:optimizations :advanced
				:pretty-print false}}]})