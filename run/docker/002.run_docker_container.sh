#!/usr/bin/env bash

docker network create --subnet 192.168.0.0/24 --gateway 192.168.0.1 apps_net

sudo docker run -d --rm --name mysqlDB \
     --net apps_net \
     -e MYSQL_DATABASE=review-db \
     -e MYSQL_USER=user01 \
     -e MYSQL_PASSWORD=user01 \
     -e MYSQL_ROOT_PASSWORD=password \
     -p 3306:3306 \
     mysql:5.7

sudo docker run -d --rm --name mongoDB \
     --net apps_net \
     -p 27017:27017 \
     mongo:3.6.9 \
     mongod --smallfiles

docker run -d --rm --name composite \
       --network apps_net \
       -e SPRING_PROFILES_ACTIVE=docker \
       -p 8080:8080 \
       composite

docker run -d --rm --name product \
      --network apps_net \
      -e SPRING_PROFILES_ACTIVE=docker \
      product

docker run -d --rm --name recommend \
       --network apps_net \
       -e SPRING_PROFILES_ACTIVE=docker \
       recommend

docker run -d --rm --name review \
       --network apps_net \
       -e SPRING_PROFILES_ACTIVE=docker \
       review