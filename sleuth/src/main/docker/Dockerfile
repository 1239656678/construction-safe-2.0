FROM openjdk:8-jdk-alpine
VOLUME /usr/cloud-sleuth/log
ADD cloud-sleuth-0.0.1-SNAPSHOT.jar cloud-sleuth.jar
EXPOSE 9411
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/cloud-sleuth.jar"]
#设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# mvn clean package docker:build -DpushImage