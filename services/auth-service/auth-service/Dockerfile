# Use OpenJDK 21
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy Maven files
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Copy source code
COPY src src

# Build the JAR
RUN ./mvnw clean package -DskipTests

# Expose the port
EXPOSE 8080

# Run the app
CMD ["java", "-jar", "target/auth-service-0.0.1-SNAPSHOT.jar"]
