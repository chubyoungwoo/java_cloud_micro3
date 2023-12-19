#!/usr/bin/env bash

docker stop composite;
docker stop product;
docker stop recommend;
docker stop review;
docker stop mongoDB;
docker stop mysqlDB;

docker rmi composite;
docker rmi product;
docker rmi recommend;
docker rmi review;

docker network rm apps_net;