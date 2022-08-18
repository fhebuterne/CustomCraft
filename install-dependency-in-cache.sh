#!/bin/bash

if [[ -d ./tmp ]]; then
  echo "remove existing tmp directory"
  rm -r ./tmp
fi

mkdir ./tmp && cd ./tmp || exit

# $1 : private cache ci url
# $2 : cache ci user
# $3 : cache ci password

# for latest version we use only private ci because it is recommended to use buildtools to download latest version and not CDN
## 1.18.2
mkdir 1.18.2 && cd 1.18.2 || exit

if [ -n "$1" ]; then
  echo "info : using private cache ci url for 1.18.2 - spigot 1.18.2"
  curl -O --silent "$1"common/1.18.2/spigot-1.18.2-R0.1-SNAPSHOT.jar -u "$2:$3"
  echo "Download spigot 1.18.2 - OK"
fi

## 1.19.2
cd ..
mkdir 1.19.2 && cd 1.19.2 || exit

if [ -n "$1" ]; then
  echo "info : using private cache ci url for 1.19.2 - spigot 1.19.2"
  curl -O --silent "$1"common/1.19.2/spigot-1.19.2-R0.1-SNAPSHOT.jar -u "$2:$3"
  echo "Download spigot 1.19 - OK"
fi