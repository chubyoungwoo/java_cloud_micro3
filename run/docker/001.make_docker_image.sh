#!/usr/bin/env bash

cd ../../
project_dir=$(pwd)

git fetch --all
git reset --hard origin/main
git pull origin main

chmod 755 ./run/local/*.sh

gradle clean
gradle build --exclude-task test

cd $project_dir/services/composite
docker build -t composite -f ./Dockerfile .

cd $project_dir/services/product
docker build -t product -f ./Dockerfile .

cd $project_dir/services/recommend
docker build -t recommend -f ./Dockerfile .

cd $project_dir/services/review
docker build -t review -f ./Dockerfile .