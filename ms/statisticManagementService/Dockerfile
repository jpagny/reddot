FROM openjdk:11-jdk
MAINTAINER Jérôme Pagny (pagny.jerome@gmail.com)

WORKDIR /app

COPY ./target/statisticManagementService-1.0.0.jar statistic_api.jar

ENTRYPOINT ["java", "-jar", "statistic_api.jar"]