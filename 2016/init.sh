#!/bin/bash

day=$(printf "%02d" $1)
mkdir -p day$day

cd day$day

touch puzzle.txt sample.txt

cp ../template.scala solution.scala
