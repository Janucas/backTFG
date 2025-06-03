# Stage de compilación
FROM maven:3.8.2-jdk-11 AS build # Puedes usar una versión más reciente de Maven y JDK
COPY . .
RUN mvn clean package -DskipTests

# Stage de empaquetado
FROM openjdk:11-jdk-slim # Puedes usar una versión más reciente de OpenJDK
COPY --from=build /target/nombre-de-tu-aplicacion.jar app.jar # Reemplaza "nombre-de-tu-aplicacion.jar" con el nombre real de tu JAR
ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
