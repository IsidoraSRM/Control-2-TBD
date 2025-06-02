FROM openjdk:17

ARG JAR_FILE=./target/*.jar

COPY ${JAR_FILE} backend-c2.jar

EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/backend-c2.jar"]