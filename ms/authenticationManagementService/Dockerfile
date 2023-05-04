FROM openjdk:11-jdk
MAINTAINER Jérôme Pagny (pagny.jerome@gmail.com)

WORKDIR /app

COPY ./target/authentication-1.0.0.jar authentication_api.jar

ENTRYPOINT ["java", "-jar", "authentication_api.jar"]