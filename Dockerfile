# syntax=docker/dockerfile:1
FROM gradle:9.3-jdk21-alpine as build

WORKDIR /app

COPY gradlew .
COPY gradle/ gradle/
COPY settings.gradle* build.gradle* gradle.properties ./


RUN --mount=type=cache,target=/home/gradle/.gradle \
    ./gradlew --no-daemon dependencies

COPY . .

RUN --mount=type=cache,target=/home/gradle/.gradle \
    ./gradlew --no-daemon clean build -x test


FROM eclipse-temurin:21-jre as app

WORKDIR /app

RUN groupadd -r appuser && useradd -r -g appuser appuser

COPY --from=build /app/build/libs/*.jar app.jar

RUN chown -R appuser:appuser /app

USER appuser

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
