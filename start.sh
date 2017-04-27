#! /usr/bin/env sh

tmux split-window -h -c '#{pane_current_path}'
tmux send-keys 'C-l' 'yarn run electron' 'C-m'
tmux select-pane -t :.+
tmux send-keys 'C-l' 'yarn run figwheel' 'C-m'
