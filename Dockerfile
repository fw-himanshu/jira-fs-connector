FROM amazoncorretto:17.0.13

WORKDIR /var/ip-worker
ADD . .

RUN ./gradlew clean build -x test
RUN ls -l build/libs
RUN pwd

COPY build/libs/demo-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]