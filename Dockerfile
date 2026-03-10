# Step 1: Build stage
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app

# Copy all Gradle files
COPY build.gradle settings.gradle /app/
COPY gradle /app/gradle
COPY gradlew /app/gradlew
COPY src /app/src

# Run build
RUN gradle bootJar --no-daemon

# Step 2: Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app

# Create data directory for Excel storage
RUN mkdir -p /app/data

# Copy jar from build stage
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8000

VOLUME /app/data

ENTRYPOINT ["java", "-jar", "app.jar"]
