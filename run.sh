#!/bin/sh
# Uncomment the following for a C program and replace a.out with target binary name your build script generates.
#./a.out $1 $2

# Uncomment the following for a Java program and replace Hello with class name of your choice. Add additional class pathes if needed.
javac -classpath ./src ./ $1 $2
