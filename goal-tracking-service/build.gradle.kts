plugins {
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
	java
}

group = "ca.gbc"
version = "0.0.1-SNAPSHOT"
description = "goal-tracking-service"

java {
	toolchain { languageVersion.set(JavaLanguageVersion.of(21)) }
}

repositories { mavenCentral() }

extra["testcontainersVersion"] = "1.19.8"

dependencies {
	// --- API & Data ---
	implementation("org.springframework.boot:spring-boot-starter-web")            // REST (MVC)
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")   // MongoDB
	implementation("org.springframework.boot:spring-boot-starter-validation")     // @Valid, constraints
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	// --- WebClient for inter-service calls ---
	implementation("org.springframework.boot:spring-boot-starter-webflux")        // provides WebClient

	// --- Lombok ---
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testCompileOnly("org.projectlombok:lombok")
	testAnnotationProcessor("org.projectlombok:lombok")

	// --- Testing ---
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	// --- Integration testing with Testcontainers (used in Step 12) ---
	testImplementation("org.testcontainers:junit-jupiter:${property("testcontainersVersion")}")
	testImplementation("org.testcontainers:mongodb:${property("testcontainersVersion")}")
	testImplementation("org.apache.httpcomponents.client5:httpclient5")

}

tasks.test {
	useJUnitPlatform()
}
