FROM maven:3.6.1-jdk-8
WORKDIR /usr/local/
RUN mkdir api/
ADD . api/
WORKDIR /usr/local/api/
RUN mvn clean install
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]