(ns electron.core)

(def electron       (js/require "electron"))
(def app            (.-app electron))
(def browser-window (.-BrowserWindow electron))

(def main-window (atom nil))

(defn init-browser []
  (reset! main-window
          (browser-window.
            (clj->js {:width 800 :height 600 :titleBarStyle "hidden"})))

  ; Path is relative to the compiled js file (main.js in our case)
  (.loadURL @main-window (str "file://" js/__dirname "/public/index.html"))
  (.setSheetOffset @main-window 70)
  (.openDevTools (.-webContents @main-window))
  (js/console.log "App initialized!")

  (.on @main-window "closed"
    #(reset! main-window nil)))

(.appendSwitch (.-commandLine app) "enable-experimental-web-platform-features")
(.appendSwitch (.-commandLine app) "remote-debugging-port" "9222")

(.on app "window-all-closed"
  #(when-not (= js/process.platform "darwin")
             (.quit app)))

(.on app "ready"
  init-browser)
