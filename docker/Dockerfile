#COMPILE ON CONTAINER

FROM maven:3.8.4-openjdk-17 as builder

WORKDIR /project

COPY backend/pom.xml /project

RUN mvn clean verify --fail-never

COPY backend/src /project/src

RUN mvn package -DskipTests


#COPY AND RUN ON APP CONTAINER

FROM openjdk:17

WORKDIR /usr/src/app

COPY --from=builder /project/target/*.jar /usr/src/app

EXPOSE 8443

CMD ["java", "-jar", "backend-0.0.1-SNAPSHOT.jar"]

