### Monolithic MMall (Using SpringBoot)


Based on [https://github.com/fenixsoft/monolithic_arch_springboot](https://github.com/fenixsoft/monolithic_arch_springboot) and made some improvements.

本项目基于周志明老师的[凤凰架构](https://icyfenix.cn/introduction/about-the-fenix-project.html)中的示例项目:[单体架构](https://github.com/fenixsoft/monolithic_arch_springboot). 在此基础上做一些改进和修改。

### Local Run - 本地调式

依赖的测试基础设施环境使用 Docker Compose 打包（见 docker-compose.yml），MySQL, Redis 等。

```shell
# prepare dev environment
docker-compose up -d

# startup application
./gradlew bootRun
```

进入主页 [http://localhost:8080/](http://localhost:8080/)  默认账号 kaybee, 密码 123456

使用`docker-compose up -d` 先启动依赖的其他基础设施服务，再运行项目 Main 方法.

进入主页 [http://localhost:8080/](http://localhost:8080/).

![home](./img/home.png)

### Technology - 技术选型

- SpringBoot
- Spring Cache + Redis
- Redisson as distributed lock
- Spring Data JPA
- Spring Security
- Spring Security OAuth 2.3
- Spring Security JWT
- Jackson
- Bean Validation 2.0 (Hibernate Validator 6)


### Frontend - 前端项目

[https://github.com/LiuKay/mmall-frontend](https://github.com/LiuKay/mmall-frontend)

### Develop Plan - 开发计划

- 单体版本使用 SpringBoot
- 微服务版本使用 Spring Cloud 体系
- K8s 版本

### Other Versions - 其他版本（分支）

- v1.0 

    单服务器 + FTP文件服务器,

    主要技术：SSM/Guava/Jackson/Joda/注解

- v2.0 

    Tomcat集群+Nginx负载均衡+Redis分布式,

    在V1.0基础上进行迭代重构，主要技术Redis 、Spring Schedule、Tomcat集群、Nginx负载均衡

- v3.0_springboot_Deprecated （已废弃）
    
    重构了登录鉴权的部分
