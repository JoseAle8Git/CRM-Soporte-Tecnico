# Dockerfile (Ubicación: ./backend/crmSoporteTecnico/crmSoporteTecnico/Dockerfile)

FROM eclipse-temurin:17-jdk-focal AS builder 
WORKDIR /app

# 🚨 Copiar archivos sin la ruta crmSoporteTecnico/
COPY pom.xml .
COPY src src/
COPY mvnw .
COPY .mvn .mvn

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jdk-focal
WORKDIR /app
EXPOSE 8080

# Asumiendo que el nombre de tu JAR es crmSoporteTecnico-0.0.1-SNAPSHOT.jar
COPY --from=builder /app/target/*.jar app.jar 

ENTRYPOINT ["java", "-jar", "app.jar"]