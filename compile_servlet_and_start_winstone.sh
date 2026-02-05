#!/bin/bash

PATHSEP=":"
if [[ $OS == "Windows_NT" ]] || [[ $OSTYPE == "cygwin" ]]
then
    PATHSEP=";"
fi


#javac -cp www/WEB-INF/lib/org.json.jar${PATHSEP}winstone.jar${PATHSEP}www/WEB-INF/classes www/WEB-INF/classes/se/yrgo/schedule/*.java
#java -jar winstone.jar --webroot=www




LIBS="www/WEB-INF/lib/org.json.jar${PATHSEP}www/WEB-INF/lib/sqlite-jdbc-3.48.0.0.jar${PATHSEP}winstone.jar"
CLASSES_DIR="www/WEB-INF/classes"

javac -cp "$LIBS${PATHSEP}$CLASSES_DIR" \
    $CLASSES_DIR/se/yrgo/schedule/data/*.java \
    $CLASSES_DIR/se/yrgo/schedule/domain/*.java \
    $CLASSES_DIR/se/yrgo/schedule/format/*.java \
    $CLASSES_DIR/se/yrgo/schedule/servlet/*.java

java -jar winstone.jar --webroot=www

