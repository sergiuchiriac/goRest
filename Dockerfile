FROM openjdk:17-alpine

WORKDIR /opt/app
ADD target/*.jar ./app.jar

EXPOSE 8080 8080
EXPOSE 8081 8081
ENTRYPOINT ["java", "-jar", "app.jar"]