#!/usr/bin/env bash

cd ../../
project_dir=$(pwd)

git fetch --all
git reset --hard origin/main
git pull origin main

chmod 755 ./run/docker/*.sh

gradle clean
gradle build --exclude-task test

# 도커허브 이미지 가져오기 에러 발생시
# sudo apt install gnome-keyring 우분투에서 설치해준다.

cd $project_dir/services/composite
docker build -t composite -f ./Dockerfile .

cd $project_dir/services/product
docker build -t product -f ./Dockerfile .

cd $project_dir/services/recommend
docker build -t recommend -f ./Dockerfile .

cd $project_dir/services/review
docker build -t review -f ./Dockerfile .