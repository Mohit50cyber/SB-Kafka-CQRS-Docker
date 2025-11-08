FROM eclipse-temurin:21-jdk
WORKDIR /UserService
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} user.jar
EXPOSE 8087
ENTRYPOINT [ "java","-jar", "user.jar"]
