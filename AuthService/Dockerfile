# ---------- BUILD STAGE ----------
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Copy Maven wrapper
COPY mvnw .
COPY .mvn .mvn

# Copy pom.xml
COPY pom.xml .

# Give execute permission to mvnw
RUN chmod +x mvnw

# Download dependencies (speeds up subsequent builds)
RUN ./mvnw dependency:go-offline -B

# Copy full source code
COPY src src

# Build application
RUN ./mvnw clean package -DskipTests


# ---------- RUN STAGE ----------
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy final JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Render uses PORT env variable → Spring must read it
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
