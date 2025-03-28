FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy pre-built JAR (built locally with `mvn package`)
COPY ./target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
# Optional: Configure runtime properties
ENV JAVA_OPTS="-Xms128m -Xmx256m"
EXPOSE 8080