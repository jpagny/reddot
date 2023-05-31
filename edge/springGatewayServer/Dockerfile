FROM adoptopenjdk/openjdk11:alpine-jre
MAINTAINER Jérôme Pagny (pagny.jerome@gmail.com)

RUN apk add --no-cache curl

WORKDIR /app
ADD ./target/springGatewayServer-1.0.0.jar spring_gateway.jar
EXPOSE 11002
ENTRYPOINT ["java", "-jar", "spring_gateway.jar"]
