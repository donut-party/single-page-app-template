#!/usr/bin/env sh

# Create a new clojure project using the single-page-app template

PROJECT_ROOT=`git rev-parse --show-toplevel`
cd $PROJECT_ROOT

rm -rf ../template-test
clojure \
  -Sdeps '{:deps {party.donut/single-page-app {:local/root "./"}}}'\
  -Tnew create \
  :template party.donut/single-page-app \
  :name donut-template-test/test-app \
  :target-dir ../template-test
