# Use OpenJDK 21
FROM eclipse-temurin:21-jdk-alpine

USER root
RUN apk update && apk add --no-cache curl
# Set working directorycd 
WORKDIR /app

# Copy only Maven wrapper and pom first
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Fetch dependencies offline (using mounted cache if available)
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src src

# Build the JAR
RUN ./mvnw clean package -DskipTests

# Expose the port
EXPOSE 8080

# Run the app
CMD ["java", "-jar", "target/recipe-service-0.0.1-SNAPSHOT.jar"]
