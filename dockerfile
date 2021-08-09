FROM maven:3.8.1-jdk-11-slim AS MAVEN_BUILD
COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn -f /build/pom.xml clean package -DskipTests

FROM openjdk:13-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG DEPENDENCY=target/dependency
COPY  --from=MAVEN_BUILD /build/target/releasetracker-0.0.1-SNAPSHOT.jar  releasetracker-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","releasetracker-0.0.1-SNAPSHOT.jar"]