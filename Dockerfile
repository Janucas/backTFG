# Stage de compilación
FROM maven:3.9.6-eclipse-temurin-11 AS build 
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage de empaquetado
FROM eclipse-temurin:11-jre-slim # Use a matching JRE image for runtime
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
