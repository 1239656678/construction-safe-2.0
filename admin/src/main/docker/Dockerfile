FROM openjdk:8-jdk-alpine
VOLUME /usr/boot-admin/log
ADD boot-admin-0.0.1-SNAPSHOT.jar boot-admin.jar
EXPOSE 8888
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/boot-admin.jar"]
#设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# mvn clean package docker:build -DpushImage