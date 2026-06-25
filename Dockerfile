ARG GRADLE_VERSION=8.14
ARG JAVA_VERSION=21
ARG AUTH_API_PORT=8090

FROM gradle:${GRADLE_VERSION}-jdk${JAVA_VERSION} AS builder

WORKDIR /app

COPY build.gradle settings.gradle* ./
COPY src ./src

RUN gradle clean bootJar --no-daemon

ARG JAVA_VERSION=21
FROM eclipse-temurin:${JAVA_VERSION}-jre-alpine

ARG AUTH_API_PORT=8090

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

RUN mkdir -p /data

ENV AUTH_DB_PATH=/data/auth.db
ENV AUTH_API_PORT=${AUTH_API_PORT}

EXPOSE ${AUTH_API_PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]

