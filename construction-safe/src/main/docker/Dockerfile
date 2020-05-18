FROM com.dico/dico-tools:0.0.3-SNAPSHOT
VOLUME /usr/construction-safe/log
ADD construction-safe-0.0.1-SNAPSHOT.jar construction-safe.jar
EXPOSE 8086
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/construction-safe.jar"]
#设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# mvn clean package docker:build -DpushImage
