# Use OpenJDK 21
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml first (for dependency caching)
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Download dependencies separately (cache layer)
RUN ./mvnw dependency:go-offline

# Copy the source code after dependencies cached
COPY src src

# Build the JAR
RUN ./mvnw clean package -DskipTests

# Expose the port
EXPOSE 8080

# Run the app
CMD ["java", "-jar", "target/auth-service-0.0.1-SNAPSHOT.jar"]
