FROM openjdk:11-jdk
MAINTAINER Jérôme Pagny (pagny.jerome@gmail.com)

WORKDIR /app

COPY ./target/replyMessageManagementService-1.0.0.jar replyMessage_api.jar

ENTRYPOINT ["java", "-jar", "replyMessage_api.jar"]