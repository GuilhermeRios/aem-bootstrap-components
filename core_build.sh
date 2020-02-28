#!/usr/bin/env bash

echo "Entering core folder"
cd core

echo "Start core bundle build"
mvn -PautoInstallBundle clean install

echo "Build complete"
