FROM openjdk:8-jdk-alpine
VOLUME /usr/cloud-config/log
ADD cloud-config-0.0.1-SNAPSHOT.jar cloud-config.jar
EXPOSE 8088
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/cloud-config.jar"]
#设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# mvn clean package docker:build -DpushImage