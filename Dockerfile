FROM gradle:jdk21 AS builder

WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

RUN chmod +x gradlew

RUN ./gradlew dependencies --no-daemon

COPY src ./src

RUN ./gradlew bootJar --no-daemon -x test

FROM eclipse-temurin:21

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar unimatch.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "unimatch.jar"]
