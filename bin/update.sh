#!/usr/bin/env sh

PROJECT_ROOT=`git rev-parse --show-toplevel`
TEMPLATE_DIR="${PROJECT_ROOT}/resources/party/donut/single_page_app"
TEMPLATE_ROOT="${TEMPLATE_DIR}/root"
TEMPLATE_SRC="${TEMPLATE_DIR}/src"
MINIMAL_ROOT="${PROJECT_ROOT}/../minimal"

cp "${MINIMAL_ROOT}/deps.edn" "${TEMPLATE_ROOT}/deps.edn"
