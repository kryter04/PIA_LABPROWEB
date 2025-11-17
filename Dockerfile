FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

COPY backend-springboot/pom.xml .
COPY backend-springboot/src ./src

RUN apk add --no-cache maven

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
