# syntax=docker/dockerfile:1.7
FROM eclipse-temurin:21-jre-jammy

ARG APP_VERSION=1.0.0
ENV APP_VERSION=${APP_VERSION}
ENV APP_HOME=/opt/app-k8-deployment
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -XX:+ExitOnOutOfMemoryError"

RUN groupadd --system --gid 10001 app-k8-deployment \
    && useradd --system --uid 10001 --gid 10001 --home-dir ${APP_HOME} --create-home app-k8-deployment

WORKDIR ${APP_HOME}
COPY application/build/libs/app-k8-deployment-health-${APP_VERSION}.jar app.jar
COPY container/entrypoint.sh entrypoint.sh

RUN chmod 0555 entrypoint.sh && chown -R 10001:10001 ${APP_HOME}

USER 10001:10001
EXPOSE 8080
ENTRYPOINT ["/opt/app-k8-deployment/entrypoint.sh"]
