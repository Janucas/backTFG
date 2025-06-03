# Stage de compilación
FROM maven:3.9.6-jdk-17 AS build 
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage de empaquetado
FROM openjdk:17-jdk-slim 
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar 

# EXPONER EL PUERTO. El puerto 8080 ya está en tu application.properties
EXPOSE 8080

# DEFINIR EL COMANDO DE ARRANQUE.
# Render inyectará las variables de entorno que configures en su dashboard.
# No necesitas definir los valores sensibles aquí.
ENTRYPOINT ["java", "-jar", "app.jar"]
