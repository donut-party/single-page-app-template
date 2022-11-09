#!/usr/bin/env sh
PROJECT_ROOT=`git rev-parse --show-toplevel`
cd $PROJECT_ROOT

rm -rf tmp/test-app
clojure \
  -Sdeps '{:deps {party.donut/single-page-app {:local/root "./"}}}'\
  -Tnew create \
  :template party.donut/single-page-app \
  :name my.org/test-app \
  :target-dir tmp/test-app
