#!/bin/bash

_CMD_JAVA=`which java`;

$_CMD_JAVA -Xmx8G -cp ./out/production/:./lib/commons-math3-3.6.1.jar RunMe $1 $2 $3 $4 $5 $6 $7 $8


