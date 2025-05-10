# Build stage
FROM gradle:8.6.0-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle build --no-daemon -x test

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

# Set environment variables (tối ưu RAM)
ENV JAVA_OPTS="-Xmx256m -Xms128m -XX:+UseSerialGC -Dspring.main.lazy-initialization=true"
ENV SPRING_PROFILES_ACTIVE=dev

# Expose the port your app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]

