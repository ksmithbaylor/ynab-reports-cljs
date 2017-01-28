(ns dev.core
  (:require [figwheel.client :as fw :include-macros true]
            [ui.core]))

(fw/watch-and-reload
 :websocket-url "ws://localhost:3449/figwheel-ws"
 :before-jsload #(.clear js/console))
