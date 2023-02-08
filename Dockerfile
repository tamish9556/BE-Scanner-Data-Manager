FROM maven:latest as build
ADD . /
RUN mvn clean package -B --no-transfer-progress
FROM bootcampdell.jfrog.io/default-docker-virtual/openjdk:17.0.1-jdk-slim
COPY --from=build app/target/app-*.jar ./app/app.jar
WORKDIR /app
#bootcampdell.jfrog.io/default-docker-virtual/backend:latest
ENTRYPOINT ["/bin/bash" , "-c", "java -jar app.jar"]