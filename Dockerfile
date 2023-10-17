FROM ubuntu:latest AS build

RUN apt-get update && apt-get install openjdk-17-jdk -y && apt-get clean

COPY . .

RUN apt-get install maven -y && apt-get clean
RUN mvn clean install

EXPOSE 8080

COPY --from=build /target/todolist-1.0.0.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
