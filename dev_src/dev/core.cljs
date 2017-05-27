(ns dev.core
  (:require [figwheel.client :as fw :include-macros true]
            [reagent.core :refer [force-update-all]]
            [ui.core]))

(fw/watch-and-reload
 :websocket-url "ws://localhost:3449/figwheel-ws"
 :before-jsload #(js/console.log
                    (apply str "%c" (repeat 80 "-"))
                    "background-color: red; color: white")
 :on-jsload #(force-update-all))
