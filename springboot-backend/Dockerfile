
# Use a Maven image to build the project
FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app

# Copy project files
COPY . .

# Build the application (skip tests if needed)
RUN mvn clean package -DskipTests

# Use a lightweight JDK runtime image
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy built JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Set environment variables (optional)
ENV JAVA_OPTS=""

# Run the Spring Boot app
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
