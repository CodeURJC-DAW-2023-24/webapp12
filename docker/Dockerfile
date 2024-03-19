#COMPILE ON CONTAINER

FROM maven:3.9.6-eclipse-temurin-21 as builder

WORKDIR /project

COPY backend/pom.xml /project/

RUN mvn clean verify --fail-never

COPY backend/src /project/src

RUN mvn package -DskipTests


#COPY AND RUN ON APP CONTAINER

FROM eclipse-temurin:21-jdk

WORKDIR /usr/src/app

COPY --from=builder /project/target/*.jar /usr/src/app

EXPOSE 8443

CMD ["java", "-jar", "yourHOmeTEL-0.0.1-SNAPSHOT.jar"]
