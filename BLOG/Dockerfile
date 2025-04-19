# Use OpenJDK 17 as the base image
FROM openjdk:17

# Expose the application port
EXPOSE 8092

# Copy the JAR file into the container
ADD target/Blog-0.0.1-SNAPSHOT.jar /Blog.jar

# Define the entry point to run the application
ENTRYPOINT ["java", "-jar", "/Blog.jar"]
