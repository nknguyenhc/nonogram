FROM openjdk:11-jre-slim

WORKDIR /app

COPY target/server.jar .

EXPOSE 8080

CMD ["java", "-jar", "server.jar"]
