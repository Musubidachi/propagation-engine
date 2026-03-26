#!/bin/sh
# Maven Wrapper script
MAVEN_PROJECTBASEDIR="${MAVEN_BASEDIR:-$(cd "$(dirname "$0")" && pwd)}"
MAVEN_WRAPPER_JAR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"

exec java \
  $MAVEN_OPTS \
  -jar "$MAVEN_WRAPPER_JAR" \
  "$@"
