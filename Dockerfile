FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN --mount=type=cache,target=/root/.m2 ./mvnw dependency:go-offline -B
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 ./mvnw -Dmaven.test.skip=true package -B \
&& java -Djarmode=tools -jar target/reception-1.0.0.jar extract --layers --launcher


FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /app
COPY --from=builder /app/reception-1.0.0/dependencies/ ./
COPY --from=builder /app/reception-1.0.0/spring-boot-loader/ ./
COPY --from=builder /app/reception-1.0.0/snapshot-dependencies/ ./
COPY --from=builder /app/reception-1.0.0/application/ ./
EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]