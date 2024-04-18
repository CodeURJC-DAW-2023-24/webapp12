#COMPILE FOR ANGULAR

FROM node:20.12.0 as angular

WORKDIR /angular


#COPY ANGULAR FILES TO CONTAINER AND INSTALL DEPENDENCIES
COPY /frontend/package.json /frontend/package-lock.json /frontend/angular.json /frontend/tsconfig.json \
/frontend/tsconfig.app.json /frontend/tsconfig.spec.json /angular/

RUN npm install

#COPY ANGULAR FRONTEND SRC FILES TO CONTAINER
COPY /frontend/src /angular/src

#BUILD ANGULAR PROJECT FOR PRODUCTION, AND RENAMES CREATED FOLDER TO NEW
RUN npm run prod && mv /angular/dist/browser /angular/dist/new


#COMPILE MVN PACKAGE ON CONTAINER

FROM maven:3.9.6-eclipse-temurin-17 as builder

WORKDIR /project

COPY backend/pom.xml /project/

RUN mvn clean verify --fail-never

COPY backend/src /project/src

#COPIES ANGULAR BUILT FILE TO SPRING BOOT STATIC FOLDER. DELETES OLD FILES BEFORE COPY FOR CLEAN BUILD
RUN rm -rf /project/src/main/resources/static/new/*
COPY --from=angular /angular/dist/new /project/src/main/resources/static/new

RUN mvn package -DskipTests


#COPY AND RUN ON APP CONTAINER

FROM eclipse-temurin:17-jdk

WORKDIR /usr/src/app

COPY --from=builder /project/target/*.jar /usr/src/app

EXPOSE 8443

CMD ["java", "-jar", "yourHOmeTEL-0.0.1-SNAPSHOT.jar"]
