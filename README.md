
#### v3.0_springboot 开发计划

   1. 集成 Swagger2, 切换 SpringBoot
   2. SpringBoot 集成 Mybaits
   3. 集成 Spring Security
   4. 配置 Spring Data Redis 或 使用 Spring Cache 管理缓存
   5. 修改所有接口为 RESTFull 风格, 摒弃正常返回时的包装对象, 定义统一的错误处理机制
   6. 支持手机验证码登录，移除cookie，使用JWT作为认证
   7. 支持本地测试，使用 testcontainers
   8. 替换maven 使用gradle 作为依赖管理



Swagger2 UI 地址：http://localhost:8081/swagger-ui.html

前端项目地址：https://github.com/LiuKay/mmall-kay-js

### mmall 项目整体模块划分

##### 1 用户模块
##### 2 分类管理模块
##### 3 产品管理模块:门户，后台
##### 4 购物车模块
##### 5 地址管理模块
##### 6 支付模块
##### 7 订单管理模块

### mmall 商城功能接口清单 

![](https://github.com/LiuKay/mmall-kay-Java/blob/master/readme-img/mmall%20项目功能接口清单.png)


### 涉及技术栈
Linux、Nginx、Mysql、Git、Maven、Java、Spring、SpringMVC、Mybatis

### 项目分支

- master 发布版本 : 目前为v2.0

- v1.0 单服务器 + FTP文件服务器

    主要技术：SSM/Guava/Jackson/Joda/注解

- v2.0 Tomcat集群+Nginx负载均衡+Redis分布式
    
    在V1.0基础上进行迭代重构，主要技术Redis 、Spring Schedule、Tomcat集群、Nginx负载均衡
    

