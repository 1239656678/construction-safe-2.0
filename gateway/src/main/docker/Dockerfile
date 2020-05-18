FROM openjdk:8-jdk-alpine
VOLUME /usr/cloud-gateway/log
ADD cloud-gateway-0.0.1-SNAPSHOT.jar cloud-gateway.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/cloud-gateway.jar"]
#设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# mvn clean package docker:build -DpushImage