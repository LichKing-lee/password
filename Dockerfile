FROM eclipse-temurin:25-jdk AS builder
WORKDIR /workspace

COPY gradlew gradlew
COPY gradle gradle
COPY settings.gradle.kts build.gradle.kts ./
COPY password-api/build.gradle.kts password-api/build.gradle.kts
COPY password-domain/build.gradle.kts password-domain/build.gradle.kts
RUN chmod +x gradlew && ./gradlew --no-daemon dependencies -q || true

COPY password-api/src password-api/src
COPY password-domain/src password-domain/src
RUN ./gradlew --no-daemon :password-api:bootJar -x test

FROM eclipse-temurin:25-jre
WORKDIR /app

RUN groupadd --system spring && useradd --system --gid spring spring
USER spring:spring

COPY --from=builder /workspace/password-api/build/libs/*.jar app.jar

ENV JAVA_OPTS=""
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
