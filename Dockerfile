FROM openjdk:17
COPY target/find-project-0.0.1-SNAPSHOT.jar teamder.jar
ENTRYPOINT ["java","-jar","/teamder.jar"]