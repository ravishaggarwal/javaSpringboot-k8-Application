# APPLICATION — auspost-health

## Responsibility
This repository owns the Java/Spring Boot application, Gradle build/tests, and Docker image. It deliberately does not own Kubernetes, Jenkins, or AWS infrastructure.

## Contents
```text
application/              Spring Boot source and Gradle build
container/Dockerfile      production image
container/entrypoint.sh   Vault/Spring-aware startup
 SPRING_CONFIG_ADDITIONAL_LOCATION - Spring Boot then reads those files in addition to the normal application.yml packaged inside your JAR.
```

## Health endpoint
`GET /health` returns success, application version and environment. The image version is injected with `APP_VERSION`, so a new deployment is visible at the endpoint.

## Build and test
```bash
cd application
./gradlew clean test componentTest build
```

Run locally:
```bash
./gradlew bootRun
curl http://localhost:8080/health
```

## Build the image
Run from this repository root after the Gradle build:
```bash
docker build --build-arg APP_VERSION=1.0.0 -f container/Dockerfile -t auspost-health:1.0.0 .
```

The CI repository tags the image with a release/build version and pushes it to `AusPostJrog/auspost-health`.

## Configuration and secrets
Runtime environment variables and Vault-rendered Spring property files are supported. The application never owns Vault credentials. Vault Kubernetes authentication and injection are defined in the Platform repository.

Expected environments: `dev`, `sit1`, `sit4`, `perf`, `prod`.

## Container security
The supplied Docker/runtime configuration uses a minimal Java runtime, a non-root user, and is compatible with Kubernetes controls such as `allowPrivilegeEscalation: false`, dropped capabilities and a read-only root filesystem. Never bake secrets or certificates into the image.

## CI contract
The CI pipeline checks out this repository and runs Gradle unit/component tests, SonarQube, Snyk scans, Docker build, container scan and JFrog push.
