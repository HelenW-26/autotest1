#!/usr/bin/env bash

#docker build -t helenwu2025/selenium_test:jdk17_chrome114_1 --platform=linux/amd64 .

#docker push helenwu2025/selenium_test:jdk17_chrome114_1

docker build -t helenwu2025/selenium_test_node:jdk17_chrome114_1 --platform=linux/amd64 .

docker push helenwu2025/selenium_test_node:jdk17_chrome114_1