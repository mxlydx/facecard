FROM harbor.gagogroup.cn/base-os/centos7_jdk8
WORKDIR /usr/local/
RUN mkdir api/
ADD . api/
WORKDIR /usr/local/api/
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

CMD ["java","-jar","app.jar"]