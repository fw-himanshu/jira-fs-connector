FROM amazoncorretto:17.0.13

WORKDIR /var/ip-worker
ADD . .

RUN ./gradlew clean build -x test
RUN echo $(ls -l .)
RUN echo $(pwd)

COPY build/libs/demo-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]