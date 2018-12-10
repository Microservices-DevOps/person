#!/bin/bash
cp ${PERSON_HOME}/person.jar bin/person.jar
java ${JVM_ARGS} -jar -Dserver.port=9090 bin/person.jar
