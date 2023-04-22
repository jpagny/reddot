FROM openjdk:11-jdk
MAINTAINER Jérôme Pagny (pagny.jerome@gmail.com)
WORKDIR /app
ADD ./target/springGatewayServer-1.0.0.jar spring_gateway.jar
EXPOSE 11002
ENTRYPOINT ["java", "-jar", "spring_gateway.jar"]
