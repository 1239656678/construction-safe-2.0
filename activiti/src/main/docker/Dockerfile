FROM com.dico/dico-tools:0.0.3-SNAPSHOT
VOLUME /usr/activiti/log
ADD activiti-0.0.1-SNAPSHOT.jar activiti.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/activiti.jar"]
#设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# mvn clean package docker:build -DpushImage
