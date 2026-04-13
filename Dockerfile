FROM eclipse-temurin:21-jdk
RUN apt-get update && apt-get install -y libfreetype6 fontconfig && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY . .
RUN ./mvnw package -DskipTests
EXPOSE 8080
ENTRYPOINT ["java", "-Djava.awt.headless=true", "-jar", "target/spotify-card-0.0.1-SNAPSHOT.jar"]
