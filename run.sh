#!/bin/bash
echo "Building and running Maven project: TAI..."

mvn clean package exec:java

if [ $? -ne 0 ]; then
  echo "Maven build/run failed. Check the output for details."
  exit 1
fi
