#!/bin/bash

if [[ -d ./libs ]]; then
  echo "remove existing libs directory"
  rm -r ./libs
fi

mkdir ./libs && cd ./libs || exit

# $1 : private cache ci url
# $2 : cache ci user
# $3 : cache ci password

# for latest version we use only private ci because it is recommended to use buildtools to download latest version and not CDN
## 1.18.2
if [ -n "$1" ]; then
  curl -O --silent "$1"common/1.18.2/spigot-1.18.2-R0.1-SNAPSHOT.jar -u "$2:$3"
  echo "Download spigot 1.18.2 - OK"
fi

## 1.19.2
if [ -n "$1" ]; then
  curl -O --silent "$1"common/1.19.2/spigot-1.19.2-R0.1-SNAPSHOT.jar -u "$2:$3"
  echo "Download spigot 1.19.2 - OK"
fi

## 1.19.3
if [ -n "$1" ]; then
  curl -O --silent "$1"common/1.19.3/spigot-1.19.3-R0.1-SNAPSHOT.jar -u "$2:$3"
  echo "Download spigot 1.19.3 - OK"
fi


## 1.19.4
if [ -n "$1" ]; then
  curl -O --silent "$1"common/1.19.4/spigot-1.19.4-R0.1-SNAPSHOT.jar -u "$2:$3"
  echo "Download spigot 1.19.4 - OK"
fi

## 1.20.1
if [ -n "$1" ]; then
  curl -O --silent "$1"common/1.20.1/spigot-1.20.1-R0.1-SNAPSHOT.jar -u "$2:$3"
  echo "Download spigot 1.20.1 - OK"
fi

## 1.20.2
if [ -n "$1" ]; then
  curl -O --silent "$1"common/1.20.2/spigot-1.20.2-R0.1-SNAPSHOT.jar -u "$2:$3"
  echo "Download spigot 1.20.2 - OK"
fi