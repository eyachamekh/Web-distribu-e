# Base image
FROM openjdk:17-jdk-slim

# Install curl
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copy your application
COPY target/Gateway-0.0.1-SNAPSHOT.jar /app/gatewayserver.jar

# Set working directory
WORKDIR /app

# Run the application with a delay
ENTRYPOINT ["sh", "-c", "sleep 30 && java -jar gatewayserver.jar"]