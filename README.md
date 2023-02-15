

# Spring Boot 2.7 Microservice Example


a description text here



 - [Architecture](./ARCHITECTURE.md)
- [Dependencies](./DEPENDENCIES.md)

#### Java Version : ***Java 8***
#### Application port : ***8500***

## How to Build the application:

```
mvn clean package
```

## How to Run the application:


### Run in your IDE:

```
run the application with dev profile, add below line to VM Options:
-Ddebug -Dspring.profiles.active=dev -Dmaven.test.skip=true -Dspring.devtools.restart.enabled=true
```
### Run in command line:

```
java -jar -Dspring.profiles.active=dev target/spring-boot-example.jar
```

### Run with docker:

```
TO COMPLETE
```

### Run with docker compose:

```
docker-compose up
```

## Application health check:

#### Global status: 
- http://localhost:8500/soshiant/sbe/actuator/health

#### Liveness: 
- http://localhost:8500/soshiant/sbe/actuator/health/liveness

####  Readiness:
- http://localhost:8500/soshiant/sbe/actuator/health/readiness

#  H2 Console:
- http://localhost:8500/soshiant/sbe/h2/
- in JDBC URL type:  
    ## jdbc:h2:mem:sbedb; 
