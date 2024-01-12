FROM openjdk:17
ADD target/authentification.jar authentification
ENTRYPOINT ["java", "-jar","authentification"]