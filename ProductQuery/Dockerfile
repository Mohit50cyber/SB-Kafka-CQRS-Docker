FROM eclipse-temurin:21-jdk
WORKDIR /ProductQuery
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} query.jar
EXPOSE 8086
ENTRYPOINT [ "java","-jar", "query.jar"]
