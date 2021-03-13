

# Spring Boot 2 Microservice Example


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
run the application with dev profile
```
### Run in commnd line:

```
java -jar -Dspring.profiles.active=dev target/emp.jar
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
- http://localhost:8500/eco/emp/actuator/health

#### Liveness: 
- http://localhost:8500/eco/emp/actuator/health/liveness

####  Readiness:
- http://localhost:8500/eco/emp/actuator/health/readiness
