(defproject ynab-helper "0.1.0-SNAPSHOT"
  :source-paths ["src"]
  :description "A helper for YNAB 4"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.456"]
                 [figwheel "0.5.8"]
                 [reagent "0.6.0"]
                 [re-frame "0.9.1"]
                 [re-frisk "0.3.2"]
                 [ring/ring-core "1.5.0"]]
  :plugins [[lein-cljsbuild "1.1.5"]
            [lein-figwheel "0.5.8"]
            [lein-doo "0.1.7"]]

  :clean-targets ^{:protect false} ["resources/main.js"
                                    "resources/public/js/ui-core.js"
                                    "resources/public/js/ui-core.js.map"
                                    "resources/public/js/ui-out"]
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
             :ring-handler tools.figwheel-middleware/app
             :server-port 3449})
