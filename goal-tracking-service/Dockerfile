# ---------- Build stage ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy Gradle wrapper and metadata first (better layer caching)
COPY gradlew gradlew.bat settings.gradle.kts build.gradle.kts ./
COPY gradle ./gradle

# Copy source
COPY src ./src

# Build fat JAR (skip tests for speed in container)
RUN ./gradlew clean bootJar -x test

# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# App runtime envs (overridable at docker run / compose)
ENV MONGO_URI="mongodb://localhost:27017" \
    MONGO_DB="goals_db" \
    WELLNESS_BASE_URL="http://localhost:8081" \
    JAVA_OPTS=""

# Copy built jar
COPY --from=build /app/build/libs/goal-tracking-service-*.jar ./app.jar

EXPOSE 8082

# (Optional) If you later add actuator, you can add a HEALTHCHECK hitting /actuator/health
# HEALTHCHECK --interval=30s --timeout=3s --start-period=30s \
#   CMD wget -qO- http://localhost:8082/actuator/health | grep '"status":"UP"' || exit 1

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar app.jar" ]
