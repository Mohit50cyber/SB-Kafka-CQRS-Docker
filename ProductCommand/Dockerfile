FROM eclipse-temurin:21-jdk
WORKDIR /ProductCommand
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} command.jar
EXPOSE 8085
ENTRYPOINT [ "java","-jar", "command.jar"]
