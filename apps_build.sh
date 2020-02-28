#!/usr/bin/env bash

echo "Entering ui.apps folder"
cd ui.apps

echo "Start apps package build"
mvn -PautoInstallPackage clean install

echo "Build complete"

