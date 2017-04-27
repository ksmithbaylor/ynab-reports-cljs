#! /usr/bin/env sh

tmux split-window -h -c '#{pane_current_path}'
tmux send-keys 'C-l' 'yarn run electron' 'C-m'

tmux split-window -c '#{pane_current_path}'
tmux send-keys 'C-l' 'yarn run less' 'C-m'

tmux select-pane -t :.+
tmux send-keys 'C-l' 'yarn run figwheel' 'C-m'
