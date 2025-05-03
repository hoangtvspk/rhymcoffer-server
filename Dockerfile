# Build stage
FROM gradle:8.6.0-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle build --no-daemon -x test

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

# Set environment variables
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=dev

# Expose the port your app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 