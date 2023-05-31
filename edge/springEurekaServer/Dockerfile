FROM openjdk:11-jdk-slim
MAINTAINER Jérôme Pagny (pagny.jerome@gmail.com)
WORKDIR /app
ADD ./target/springEurekaServer-1.0.0.jar spring_eureka.jar
EXPOSE 11001
ENTRYPOINT ["java", "-jar", "spring_eureka.jar"]
