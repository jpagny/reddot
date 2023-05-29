FROM openjdk:11-jdk
MAINTAINER Jérôme Pagny (pagny.jerome@gmail.com)

WORKDIR /app

COPY ./target/threadManagementService-1.0.0.jar thread_api.jar

ENTRYPOINT ["java", "-jar", "thread_api.jar"]