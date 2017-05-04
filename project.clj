(defproject ynab-helper "0.1.0-SNAPSHOT"
  :source-paths ["src" "ui_src" "electron_src" "dev_src"]
  :description "A helper for YNAB 4"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.521"]
                 [org.clojure/tools.nrepl "0.2.13"]
                 [com.cemerick/piggieback "0.2.1"]
                 [figwheel "0.5.10"]
                 [figwheel-sidecar "0.5.10"]
                 [reagent "0.6.1"]
                 [re-frame "0.9.2"]
                 [re-frisk "0.4.5"]
                 [binaryage/devtools "0.9.4"]
                 [ring/ring-core "1.5.1"]
                 [cljsjs/antd "2.8.0-0"]]
  :plugins [[lein-cljsbuild "1.1.5"]
            [lein-figwheel "0.5.10"]
            [lein-doo "0.1.7"]]

  :clean-targets ^{:protect false} ["resources/main.js"
                                    "resources/public/js/ui-core.js"
                                    "resources/public/js/ui-core.js.map"
                                    "resources/public/js/ui-out"]
  :repl-options {:port 8230
                 :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
  :cljsbuild
  {:builds
   [{:source-paths ["electron_src"]
     :id "electron-dev"
     :compiler {:output-to "resources/main.js"
                :output-dir "resources/public/js/electron-dev"
                :optimizations :simple
                :pretty-print true
                :cache-analysis true
                :closure-defines {"goog.DEBUG" true}}}
    {:source-paths ["ui_src" "dev_src"]
     :id "frontend-dev"
     :compiler {:output-to "resources/public/js/ui-core.js"
                :output-dir "resources/public/js/ui-out"
                :source-map true
                :asset-path "js/ui-out"
                :optimizations :none
                :cache-analysis true
                :main "dev.core"
                :closure-defines {"goog.DEBUG" true}}}
    {:source-paths ["electron_src"]
     :id "electron-release"
     :compiler {:output-to "resources/main.js"
                :output-dir "resources/public/js/electron-release"
                :optimizations :simple
                :pretty-print true
                :cache-analysis true
                :closure-defines {"goog.DEBUG" false}}}
    {:source-paths ["ui_src"]
     :id "frontend-release"
     :compiler {:output-to "resources/public/js/ui-core.js"
                :output-dir "resources/public/js/ui-release-out"
                :source-map "resources/public/js/ui-core.js.map"
                :optimizations :simple
                :cache-analysis true
                :closure-defines {"goog.DEBUG" false}
                :main "ui.core"}}
    {:source-paths ["test"]
     :id "test"
     :compiler {:output-to "resources/public/js/tests.js"
                :main ui.test-runner
                :target :nodejs
                :optimizations :none}}]}
  :doo {:build "test"
        :alias {:default [:node]}}
  :figwheel {:http-server-root "public"
             :css-dirs ["resources/public/css"]
             :ring-handler tools.figwheel-middleware/app
             :server-port 3449
             :nrepl-port 8230
             :nrepl-middleware ["cemerick.piggieback/wrap-cljs-repl"]
             :repl false})
