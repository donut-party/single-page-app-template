#!/usr/bin/env sh
PROJECT_ROOT=`git rev-parse --show-toplevel`
cd $PROJECT_ROOT

clojure \
  -Sdeps '{:deps {party.donut/single-page-app {:local/root "./"}}}'\
  -Tnew create \
  :template party.donut/single-page-app \
  :name party.donut/test-app \
  :target-dir tmp/test-app
