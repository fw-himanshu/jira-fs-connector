FROM amazoncorretto:17.0.13

WORKDIR /var/ip-worker
ADD . .
RUN echo $(ls -l .)
RUN echo $(pwd)

RUN ./gradle wrapper
RUN ./gradlew clean build -x test

#COPY ./build/libs/demo-0.0.1-SNAPSHOT.jar /app.jar

ENTRYPOINT ["java", "-jar", "./build/libs/demo-0.0.1-SNAPSHOT.jar"]