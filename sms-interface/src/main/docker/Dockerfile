FROM com.dico/dico-tools:0.0.3-SNAPSHOT
VOLUME /usr/sms-interface/log
ADD sms-interface-0.0.1-SNAPSHOT.jar sms-interface.jar
EXPOSE 8081
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/sms-interface.jar"]
#设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# mvn clean package docker:build -DpushImage
