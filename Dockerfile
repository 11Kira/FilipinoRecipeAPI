# Step 1: Build the application
FROM eclipse-temurin:17-jdk-alpine AS build
COPY . .
RUN ./gradlew bootJar --no-daemon

# Step 2: Run the application
FROM eclipse-temurin:17-jre-alpine
COPY --from=build /build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]