FROM openjdk:11-jdk
MAINTAINER Jérôme Pagny (pagny.jerome@gmail.com)

WORKDIR /app

COPY ./target/userManagementService-1.0.0.jar user_api.jar

ENTRYPOINT ["java", "-jar", "user_api.jar"]