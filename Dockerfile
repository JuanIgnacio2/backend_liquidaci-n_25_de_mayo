# Usamos una imagen oficial de Maven para construir el JAR
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos pom.xml primero para cache de dependencias
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

# Copiamos el código fuente
COPY src ./src

# Construimos el JAR (sin tests para evitar fallos)
RUN mvn -B -DskipTests clean package

# Imagen final
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copiamos el JAR generado
COPY --from=build /app/target/*.jar app.jar

# Railway asignará el puerto vía la variable PORT
EXPOSE 8080
ENV PORT=8080

CMD ["sh", "-c", "java -jar app.jar --server.port=${PORT}"]