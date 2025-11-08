FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# Build arg for JAR path created by Gradle
ARG JAR_FILE=build/libs/event-service-0.0.1-SNAPSHOT.jar

# Copy the jar from the builder stage (we'll build it via Gradle outside)
COPY ${JAR_FILE} app.jar

EXPOSE 8083
ENTRYPOINT ["java","-jar","/app/app.jar"]
