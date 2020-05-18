# dico-admin

   Spring Cloud 快速搭建微服务<br/>
    
#### 项目介绍
   dico-admin,缔科微服务框架,为开发人员提供了快速构建分布式系统的一些工具，包括配置管理、服务发现、断路器、路由、分布式会话、代码生成等。此项目为解决陕西缔科网络科技有限公司不断发展的业务而生！

#### 软件架构
    dico-admin
    |── cloud-eureka -- Netflix Eureka服务注册中心        [端口 8761]
    | 
    |── cloud-gateway -- 服务网关（springcloud gateway）      [端口 8080]
    |
    |── cloud-config -- 分布式配置中心(springcloud config)     [端口 8088]
    |
    |── cloud-sleuth -- 服务链路追踪(Spring Cloud Sleuth)     [端口 9411]
    |
    |── boot-admin -- 服务监控（springboot admin）        [端口 8888]
    |
    |—— tx-manager -- 分布式事务服务（LCN tx-manager）       [端口 8899]
    |
    |—— generator-project -- 业务代码生成模块
    |
    |—— doc -- 所需jar包和部分sql文件
    |
    |—— dico-file -- 文件上传业务代码       [端口 8989]
    |
    |—— dico-system -- 系统管理模块代码包含用户、角色、资源、等     [端口 8085]
    |
    |—— dico-organization -- 组织机构模块     [端口 8087]
    |
    |—— dico-data-dictionary -- 数据字典模块     [端口 8089]
    |
    |—— dico-notice-message -- 消息通知模块代码     [端口 8086]

#### 安装教程

1. 复制/doc/jar/中所有文件放入本地maven仓库中。
2. 创建所有数据库，并使用已有的sql文件创建表。
3. 将项目导入idea，依次启动eureka、gateway、tx-manager。

#### 开发视图

![用户业务模块](https://images.gitee.com/uploads/images/2019/0118/112159_fe30dead_1367572.png "用户业务模块")
![dico-system配置文件](https://images.gitee.com/uploads/images/2019/0118/112215_4d3b83eb_1367572.png "dico-system配置文件")

#### 管理视图

![eureka视图](https://images.gitee.com/uploads/images/2019/0118/112427_5b911fd3_1367572.png "eureka视图")
![bootadmin视图](https://images.gitee.com/uploads/images/2019/0118/112447_7015d3be_1367572.png "bootadmin视图")
![cloud-sleuth视图](https://images.gitee.com/uploads/images/2019/0118/112456_881e9a8a_1367572.png "cloud-sleuth视图")
![分布式事务管理视图](https://images.gitee.com/uploads/images/2019/0118/112844_89b62788_1367572.png "分布式事务管理视图")

#### 参与贡献

[高大陆](https://gitee.com/Gaodl)

## 本项目将持续更新！！！

