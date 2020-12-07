
#### v3.0_springboot 开发计划

1. 从 SSM 切换到 SpringBoot
2. SpringBoot 集成 Mybatis
3. 集成 Spring Security, 支持手机验证码和用户密码登录，使用JWT
4. 配置 Spring Data Redis 或 使用 Spring Cache 管理缓存
5. 修改所有接口为 RESTFull 风格, 接口成功时只返回数据,错误时通过HTTP状态码和业务状态码处理,摒弃丑陋的包装对象,定义统一的错误处理机制
6. 支持本地测试，使用 Testcontainers 来Mock MySQL, Redis 
7. maven 切换成 gradle
8. 修改原有的代码风格，遵循 google java code style


Swagger2 UI 地址：http://localhost:8081/swagger-ui/#/

### mmall 项目整体模块划分

##### 1 用户模块
##### 2 分类管理模块
##### 3 产品管理模块:门户，后台
##### 4 购物车模块
##### 5 地址管理模块
##### 6 支付模块
##### 7 订单管理模块


### 项目分支

- master 发布版本 : 目前为v2.0

- v1.0 单服务器 + FTP文件服务器

    主要技术：SSM/Guava/Jackson/Joda/注解

- v2.0 Tomcat集群+Nginx负载均衡+Redis分布式
    
    在V1.0基础上进行迭代重构，主要技术Redis 、Spring Schedule、Tomcat集群、Nginx负载均衡
    

