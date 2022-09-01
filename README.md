# MMall 

A simple project to learn different architectures.

## 这是什么

Mmall 是一个十分简化的商城项目，仅包含了用户、支付、库存管理等能支撑一个购物流程的业务功能，同时也包括了一些非业务功能，
包括登录、身份认证、鉴权等。通过一些简化，从而来更好的学习不同的架构风格是什么样的。从单体服务风格，到微服务架构，再到
云原生，不同的架构为了架构本身的问题而引入了一些新的组件。

## 当前分支版本:

Microservices SpringCloud - 微服务 SpringCloud 版

## Technology - 技术选型

- SpringBoot
- Spring Cache + Redis
- Redisson as distributed lock
- Spring Data JPA
- Spring Security
- Spring Security OAuth 2.3
- Spring Security JWT
- Jackson
- Bean Validation 2.0 (Hibernate Validator 6)
- Netflix Zuul
- Netflix Eureka
- Netflix Feign
- Spring Config

## Develop Plan - 开发计划

- 单体版本使用 SpringBoot (已完成)，分支： monolithic_springboot
- 微服务版本使用 Spring Cloud 体系 （已完成），分支：microservices_springcloud
- K8s 版本 （开发中）

Based on [https://github.com/fenixsoft/monolithic_arch_springboot](https://github.com/fenixsoft/monolithic_arch_springboot) and made some improvements.

本项目基于周志明老师的[凤凰架构](https://icyfenix.cn/introduction/about-the-fenix-project.html)中的示例项目:[单体架构](https://github.com/fenixsoft/monolithic_arch_springboot). 在此基础上做一些改进和修改。

### Local Run - 本地调式

依赖的测试基础设施环境使用 Docker Compose 打包（见 docker-compose.yml），MySQL, Redis 等。

```shell
# prepare dev environment
docker-compose up -d

# startup application
startup configuration first and then resgitry project.

and then startup other services.
```

进入主页 [http://localhost:8080/](http://localhost:8080/)  默认账号 kaybee, 密码 123456

![home](./img/home.png)


## Frontend - 前端项目

[https://github.com/LiuKay/mmall-frontend](https://github.com/LiuKay/mmall-frontend)

## Other Versions - 其他版本（分支）

- v1.0

  单服务器 + FTP文件服务器,

  主要技术：SSM/Guava/Jackson/Joda/注解

- v2.0

  Tomcat集群+Nginx负载均衡+Redis分布式,

  在V1.0基础上进行迭代重构，主要技术Redis 、Spring Schedule、Tomcat集群、Nginx负载均衡

- v3.0_springboot_Deprecated （已废弃）

  重构了登录鉴权的部分

