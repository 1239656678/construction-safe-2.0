FROM openjdk:8-jdk-alpine
VOLUME /usr/cloud-eureka/log
ADD cloud-eureka-0.0.1-SNAPSHOT.jar cloud-eureka.jar
EXPOSE 8761
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/cloud-eureka.jar"]
ENV LANG C.UTF-8
#设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# mvn clean package docker:build -DpushImage