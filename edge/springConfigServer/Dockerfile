FROM adoptopenjdk/openjdk11:alpine-jre
MAINTAINER Jérôme Pagny (pagny.jerome@gmail.com)

RUN apk add --no-cache curl

WORKDIR /app

COPY ./target/springConfigServer-1.0.0.jar springConfigServer.jar

ENTRYPOINT ["java", "-jar", "springConfigServer.jar"]