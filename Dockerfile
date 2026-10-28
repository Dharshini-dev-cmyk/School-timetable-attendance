# ============================================================
# Stage 1: Build
# ============================================================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml first for dependency caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests -B

# ============================================================
# Stage 2: Runtime
# ============================================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create non-root user for security
RUN addgroup -S springapp && adduser -S springapp -G springapp

# Copy built jar from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Set ownership
RUN chown springapp:springapp app.jar

USER springapp

# Default profile: 'docker' — connects to the MySQL container.
# Override with -e SPRING_PROFILES_ACTIVE=local if needed.
ENV SPRING_PROFILES_ACTIVE=docker

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
