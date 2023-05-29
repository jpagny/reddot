FROM openjdk:11-jdk
MAINTAINER Jérôme Pagny (pagny.jerome@gmail.com)

WORKDIR /app

COPY ./target/boardManagementService-1.0.0.jar board_api.jar

ENTRYPOINT ["java", "-jar", "board_api.jar"]