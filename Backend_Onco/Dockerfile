FROM openjdk:22-jdk
ARG JAR_FILE=target/Backend_Onco-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} oncontigo.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/oncontigo.jar"]