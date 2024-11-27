FROM amazoncorretto:17.0.13 AS build
WORKDIR /var/ip-worker
ADD . .
RUN ./gradlew clean build -x test

# Run stage
FROM amazoncorretto:17.0.13
WORKDIR /var/ip-worker
COPY --from=build /var/ip-worker/build/libs/demo-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
