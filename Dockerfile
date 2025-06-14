# Stage 1: Build the application
FROM gradle:7.5.1-jdk17 AS build

# Set working directory inside the container
WORKDIR /app

# Copy Gradle wrapper and build files first for better layer caching
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradle.properties .

# Make gradlew executable
RUN chmod +x ./gradlew

# Download Gradle dependencies (separate layer for better caching)
RUN ./gradlew dependencies --no-daemon

# Copy the rest of the application source code
COPY src ./src

# Build the Kotlin Spring Boot application
RUN ./gradlew bootJar --no-daemon -x test

# Stage 2: Create the final runtime image
FROM openjdk:17-jdk-alpine

# Add metadata labels for Docker Hub
LABEL maintainer="thepeaceboard"
LABEL version="1.0"
LABEL description="OTP Service - A reactive microservice for OTP generation and validation"
LABEL org.opencontainers.image.source="https://github.com/thepeaceboard/otp-service"
LABEL org.opencontainers.image.url="https://hub.docker.com/r/thepeaceboard/otp-service"
LABEL org.opencontainers.image.documentation="https://hub.docker.com/r/thepeaceboard/otp-service"
LABEL org.opencontainers.image.vendor="ThePeaceBoard"
LABEL org.opencontainers.image.title="OTP Service"

# Create non-root user for security
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Set working directory inside the final container
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Change ownership to non-root user
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Expose the application port
EXPOSE 3002

# Set environment variables using Docker secrets
# These will be populated when the container is run, you should reference the Kubernetes secrets
# ENV DB_HOST=${DB_HOST} \
#     DB_PASSWORD=${DB_PASSWORD}\
#     DB_PORT=${DB_PORT} \
#     DB_USERNAME=${DB_USERNAME} \
#     TWILIO_ACCOUNT_SID=${TWILIO_ACCOUNT_SID} \
#     TWILIO_AUTH_TOKEN=${TWILIO_AUTH_TOKEN} \
#     TWILIO_MESSAGING_SERVICE_SID=${TWILIO_MESSAGING_SERVICE_SID} \
#     USER_SERVICE_BASE_URL=${USER_SERVICE_BASE_URL} \
#     USER_SERVICE_PASSWORD=${USER_SERVICE_PASSWORD} \
#     USER_SERVICE_USERNAME=${USER_SERVICE_USERNAME} \
#     TWILIO_BASE_URL=https://api.twilio.com/2010-04-01
# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:3002/actuator/health || exit 1

# Set JVM options for containerized environment
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=70.0 -XX:+UseG1GC"

# Run the jar file with optimized JVM settings
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
