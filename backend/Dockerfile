FROM openjdk:11
VOLUME /tmp
EXPOSE 8080
ADD ./target/fvcatalog-0.0.1-SNAPSHOT.jar fvcatalog-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/fvcatalog-0.0.1-SNAPSHOT.jar"]