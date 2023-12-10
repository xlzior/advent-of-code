#!/bin/bash

day=$(printf "%02d" $1)
mkdir -p day$day

cd day$day

touch 1.in 1.out puzzle.in

cp ../template.scala solution.scala
