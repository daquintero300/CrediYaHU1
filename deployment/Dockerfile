FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app

COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .
COPY main.gradle .
RUN ./gradlew dependencies --no-daemon

COPY . .
RUN ./gradlew bootJar --no-daemon -x validateStructure

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /CrediYa

RUN addgroup -S appuser && adduser -S appuser -G appuser
COPY --from=builder /app/applications/app-service/build/libs/*.jar autenticacion.jar
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"

USER appuser
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -jar autenticacion.jar"]
