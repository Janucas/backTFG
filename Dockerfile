# Stage de compilación
FROM maven:3.9.6-jdk-17 AS build # Es recomendable usar una versión de JDK más reciente y estable, como JDK 17
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage de empaquetado
FROM openjdk:17-jdk-slim # Usamos la misma versión de JDK que en la etapa de compilación
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar # Asegúrate de que esto coincida con el nombre de tu JAR, o usa *.jar para mayor flexibilidad

# EXPONER EL PUERTO. El puerto 8080 ya está en tu application.properties
EXPOSE 8080

# DEFINIR EL COMANDO DE ARRANQUE.
# Render inyectará las variables de entorno que configures en su dashboard.
# No necesitas definir los valores sensibles aquí.
ENTRYPOINT ["java", "-jar", "app.jar"]
