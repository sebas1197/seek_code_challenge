# syntax=docker/dockerfile:1
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
# Puerto de Cloud Run (env $PORT), no hard‑coded
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
