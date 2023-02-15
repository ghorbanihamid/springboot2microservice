FROM openjdk:8-jre-alpine

ENV AWS_REGION us-east-1

ENV AWS_ACCESS_KEY_ID LKIDAX3PHRTG5L4Z46HJ

ENV AWS_SECRET_ACCESS_KEY WKfWNBGH8BlLY8OIDavpufBkjcy4l3TJdQJpnZEn

WORKDIR /soshiant/springbootexample

COPY target/spring-boot-example.jar bin/spring-boot-example.jar

EXPOSE 8500

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","bin/spring-boot-example.jar"]