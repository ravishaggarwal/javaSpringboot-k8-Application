#!/bin/sh
set -eu
CONFIG_LOCATIONS=""
if [ -f /vault/secrets/application.properties ]; then
  CONFIG_LOCATIONS="optional:file:/vault/secrets/application.properties"
fi
if [ -f /vault/secrets/sec.properties ]; then
  if [ -n "$CONFIG_LOCATIONS" ]; then CONFIG_LOCATIONS="$CONFIG_LOCATIONS,"; fi
  CONFIG_LOCATIONS="${CONFIG_LOCATIONS}optional:file:/vault/secrets/sec.properties"
fi
if [ -n "$CONFIG_LOCATIONS" ]; then
  export SPRING_CONFIG_ADDITIONAL_LOCATION="$CONFIG_LOCATIONS"
fi
exec java ${JAVA_TOOL_OPTIONS:-} -jar /opt/app-k8-deployment/app.jar
