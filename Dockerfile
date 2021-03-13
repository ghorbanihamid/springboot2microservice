FROM openjdk:8-jre-alpine

WORKDIR /soshiant/springbootexample

COPY target/spring-boot-example.jar bin/spring-boot-example.jar

EXPOSE 8500

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","bin/spring-boot-example.jar"]