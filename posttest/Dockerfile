FROM amazoncorretto:17.0.9-alpine3.18

WORKDIR /app
ADD . .
RUN ["./gradlew","bootJar"]
ENTRYPOINT ["java", "-jar", "build/libs/posttest-0.0.1-SNAPSHOT.jar"]


# stage-1 build artifact
FROM amazoncorretto:17.0.9-alpine3.18 as builder
WORKDIR /app
ADD . .
RUN ["./gradlew", "clean",  "bootJar"]

# stage-2 running image
FROM gcr.io/distroless/java17-debian12:latest
WORKDIR /app
COPY --from=builder /app/build/libs/posttest-0.0.1-SNAPSHOT.jar posttest-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "posttest-0.0.1-SNAPSHOT.jar"]