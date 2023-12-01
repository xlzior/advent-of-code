#!/bin/bash

scala-cli ../util solution.scala -- $1.txt ${@:2}
