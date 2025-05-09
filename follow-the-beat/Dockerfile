# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Accept GitHub credentials as build args
ARG GITHUB_USER
ARG GITHUB_TOKEN

# Copy your custom Maven settings file (make sure it references the ARGs)
COPY maven/settings.xml /root/.m2/settings.xml

# Copy project files
COPY pom.xml .
COPY src ./src

# Build the app
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8060
ENTRYPOINT ["java", "-jar", "app.jar"]
