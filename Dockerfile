FROM amazoncorretto:17.0.13

WORKDIR /var/ip-worker
COPY . .

RUN ./gradlew clean build -x test
RUN ls -l build/libs
RUN pwd

# Add the JAR file to the container
COPY build/libs/demo-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]