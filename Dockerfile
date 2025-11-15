# Stage 1: Build
FROM maven:3.8.8-eclipse-temurin-17 AS build

WORKDIR /workspace

# Копируем pom.xml и скачиваем зависимости
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Копируем исходники и собираем jar
COPY src ./src
RUN mvn package -DskipTests -B

# Stage 2: Run
FROM eclipse-temurin:17-jre

WORKDIR /app

# Копируем jar из build stage
COPY --from=build /workspace/target/*.jar app.jar

# Устанавливаем точку входа
ENTRYPOINT ["java","-jar","app.jar"]