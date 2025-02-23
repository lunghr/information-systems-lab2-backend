FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
COPY . .
RUN ./gradlew clean build bootRun -x test
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "./build/libs/information-systems-lab1-0.0.1-SNAPSHOT.jar"]