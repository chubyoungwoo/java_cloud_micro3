#!/usr/bin/env bash

cd ../../
git fetch --all
git reset --hard origin/main
git pull origin main

chmod 755 ./run/local/*.sh

gradle clean
gradle build --exclude-task test



