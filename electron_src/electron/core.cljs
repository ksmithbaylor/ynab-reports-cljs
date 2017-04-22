(ns electron.core)

(def electron       (js/require "electron"))
(def app            (.-app electron))
(def browser-window (.-BrowserWindow electron))

(def main-window (atom nil))

(defn init-browser []
  (reset! main-window (browser-window.
                        (clj->js {:width 800
                                  :height 600})))
  ; Path is relative to the compiled js file (main.js in our case)
  (.loadURL @main-window (str "file://" js/__dirname "/public/index.html"))
  (.openDevTools (.-webContents @main-window))
  (.log js/console "App initialized!")

  (.on @main-window "closed"
    #(reset! main-window nil)))

(.on app "window-all-closed"
  #(when-not (= js/process.platform "darwin")
             (.quit app)))

(.on app "ready"
  init-browser)
