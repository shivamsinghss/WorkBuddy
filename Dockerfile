# ── Stage 1: Build ──────────────────────────────────────────
FROM gradle:8.7-jdk17 AS builder

WORKDIR /app

# Copy dependency files first for layer caching
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# Download dependencies (cached unless build.gradle changes)
RUN gradle dependencies --no-daemon || true

# Copy source and build
COPY src ./src
RUN gradle bootJar --no-daemon

# ── Stage 2: Run ─────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create data directory for Excel storage
RUN mkdir -p /app/data

# Copy the built JAR from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose the application port
EXPOSE 8000

# Mount point for persisting Excel data files
VOLUME /app/data

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
