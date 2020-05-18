FROM frolvlad/alpine-java:jdk8-slim
VOLUME /usr/dico-base/log
ADD dico-base-0.0.1-SNAPSHOT.jar dico-base.jar
EXPOSE 8085
#设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# 二维码生成依赖
RUN echo -e "https://mirror.tuna.tsinghua.edu.cn/alpine/v3.4/main\n\
https://mirror.tuna.tsinghua.edu.cn/alpine/v3.4/community" > /etc/apk/repositories
RUN apk --update add curl bash ttf-dejavu && \
      rm -rf /var/cache/apk/*
# 二维码生成依赖
## 二维码生成中文乱码
#设置字符集
ENV LANG en_US.UTF-8
#安装字体软件，完成字体配置
RUN apk add --update ttf-dejavu fontconfig && rm -rf /var/cache/apk/*
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/dico-base.jar"]
# mvn clean package docker:build -DpushImage
# 容器创建之后需要上传字体文件 命令如下：
# docker cp /usr/share/fonts/SIMKAI.TTF 容器ID:/usr/share/fonts/ttf-dejavu/SIMKAI.TTF
