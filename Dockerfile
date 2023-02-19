FROM openjdk:17-alpine
COPY target/my-dictionary-0.0.1-SNAPSHOT.jar my-dictionary.jar
ENTRYPOINT ["java","-jar","/my-dictionary.jar"]