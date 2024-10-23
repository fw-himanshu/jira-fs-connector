FROM amazoncorretto:17.0.13

WORKDIR /var/ip-worker
ADD . .

RUN ./gradlew clean build -x test
ENTRYPOINT ["java", "-jar", "./build/libs/demo-0.0.1-SNAPSHOT.jar"]