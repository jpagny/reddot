FROM openjdk:11-jdk
MAINTAINER Jérôme Pagny (pagny.jerome@gmail.com)

WORKDIR /app

COPY ./target/topicManagementService-1.0.0.jar topic_api.jar

ENTRYPOINT ["java", "-jar", "topic_api.jar"]