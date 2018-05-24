

线上暂时测试地址：http://116.62.192.142:8081/dist/view/index.html
账号:liukai 密码：123456

由于没有配置域名等原因，支付宝支付二维码以及图片服务器没有配置出来

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

- master 发布版本

- v1.0 单服务器 + FTP文件服务器

    主要技术：SSM/Guava/Jackson/Joda/注解

- v2.0 Tomcat集群+Nginx负载均衡+Redis分布式
    
    在V1.0基础上进行迭代重构，主要技术Redis 、Spring Schedule、Tomcat集群、Nginx负载均衡