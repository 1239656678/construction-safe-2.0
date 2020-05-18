FROM com.dico/dico-tools:0.0.3-SNAPSHOT
VOLUME /usr/${projectName}/log
ADD ${projectName}-0.0.1-SNAPSHOT.jar ${projectName}.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/${projectName}.jar"]
#设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# mvn clean package docker:build -DpushImage