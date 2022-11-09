#!/usr/bin/env sh

PROJECT_ROOT=`git rev-parse --show-toplevel`
TEMPLATE_DIR="${PROJECT_ROOT}/resources/party/donut/single_page_app"
TEMPLATE_ROOT="${TEMPLATE_DIR}/root"
MINIMAL_ROOT="${PROJECT_ROOT}/../minimal"

mv $TEMPLATE_DIR/{template.edn,../template.edn}

rm -Rf $TEMPLATE_DIR
mkdir -p "${TEMPLATE_DIR}/root/src/donut"
mv $TEMPLATE_DIR/{../template.edn,template.edn}

cp "${MINIMAL_ROOT}/.gitignore" "${TEMPLATE_DIR}/root/.gitignore"
cp "${MINIMAL_ROOT}/build.clj" "${TEMPLATE_DIR}/root/build.clj"
cp "${MINIMAL_ROOT}/deps.edn" "${TEMPLATE_DIR}/root/deps.edn"
cp "${MINIMAL_ROOT}/package.json" "${TEMPLATE_DIR}/root/package.json"
cp "${MINIMAL_ROOT}/shadow-cljs.edn" "${TEMPLATE_DIR}/root/shadow-cljs.edn"
cp "${MINIMAL_ROOT}/CHANGELOG.md" "${TEMPLATE_DIR}/root/CHANGELOG.md"
cp "${MINIMAL_ROOT}/README.md" "${TEMPLATE_DIR}/root/README.md"
cp "${MINIMAL_ROOT}/src/donut/hooks.clj" "${TEMPLATE_DIR}/root/src/donut/hooks.clj"
cp -r  "${MINIMAL_ROOT}/doc" "${TEMPLATE_DIR}/root/doc"
cp -r  "${MINIMAL_ROOT}/resources" "${TEMPLATE_DIR}/root/resources"
cp -r  "${MINIMAL_ROOT}/dev" "${TEMPLATE_DIR}/dev"
cp -r  "${MINIMAL_ROOT}/src/donut/minimal" "${TEMPLATE_DIR}/src"
cp -r  "${MINIMAL_ROOT}/test/donut/minimal" "${TEMPLATE_DIR}/test"

cd $TEMPLATE_DIR
find ./ ! -name '.DS_Store' -type f -exec sed -i "" 's/donut\.minimal/{{top\/ns}}/g' "{}" \;
