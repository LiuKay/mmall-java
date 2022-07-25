
INSERT INTO account
VALUES (1, 'kaybee', '$2a$10$SvyLUrNMbwPAnPBvDBbItOtLnmg88IbKFnBQy4zjaRNCCi0p5OCya', 'kaybee', '', '18888888888',
        'kaybee@gmail.com', '北京市');

INSERT INTO mmall.address (id, city, details, district, phone, province, receiver_name, user_id, zip) VALUES (1, '北京市', 'XXXXXX', null, '18888888887', '北京市', 'kay', 1, '10000');

INSERT INTO mmall.product
VALUES (1, '深入理解Java虚拟机（第3版）', 129,
        '<p>这是一部从工作原理和工程实践两个维度深入剖析JVM的著作，是计算机领域公认的经典，繁体版在台湾也颇受欢迎。</p><p>自2011年上市以来，前两个版本累计印刷36次，销量超过30万册，两家主要网络书店的评论近90000条，内容上近乎零差评，是原创计算机图书领域不可逾越的丰碑，第3版在第2版的基础上做了重大修订，内容更丰富、实战性更强：根据新版JDK对内容进行了全方位的修订和升级，围绕新技术和生产实践新增逾10万字，包含近50%的全新内容，并对第2版中含糊、瑕疵和错误内容进行了修正。</p><p>全书一共13章，分为五大部分：</p><p>第一部分（第1章）走近Java</p><p>系统介绍了Java的技术体系、发展历程、虚拟机家族，以及动手编译JDK，了解这部分内容能对学习JVM提供良好的指引。</p><p>第二部分（第2~5章）自动内存管理</p><p>详细讲解了Java的内存区域与内存溢出、垃圾收集器与内存分配策略、虚拟机性能监控与故障排除等与自动内存管理相关的内容，以及10余个经典的性能优化案例和优化方法；</p><p>第三部分（第6~9章）虚拟机执行子系统</p><p>深入分析了虚拟机执行子系统，包括类文件结构、虚拟机类加载机制、虚拟机字节码执行引擎，以及多个类加载及其执行子系统的实战案例；</p><p>第四部分（第10~11章）程序编译与代码优化</p><p>详细讲解了程序的前、后端编译与优化，包括前端的易用性优化措施，如泛型、主动装箱拆箱、条件编译等的内容的深入分析；以及后端的性能优化措施，如虚拟机的热点探测方法、HotSpot 的即时编译器、提前编译器，以及各种常见的编译期优化技术；</p><p>第五部分（第12~13章）高效并发</p><p>主要讲解了Java实现高并发的原理，包括Java的内存模型、线程与协程，以及线程安全和锁优化。</p><p>全书以实战为导向，通过大量与实际生产环境相结合的案例分析和展示了解决各种Java技术难题的方案和技巧。</p>',
        '/static/cover/jvm3.jpg', '/static/desc/jvm3.jpg');
INSERT INTO mmall.product VALUES (2, '测试商品1', 10, '<p>测试描述</p>', '/static/cover/jvm3.jpg', '/static/desc/jvm3.jpg');
INSERT INTO mmall.product VALUES (3, '测试商品2', 15, '<p>测试描述</p>', '/static/cover/jvm3.jpg', '/static/desc/jvm3.jpg');

INSERT INTO wallet VALUES (1, 10000, 1);

insert into stockpile values (1, 100, 0, 1);
insert into stockpile values (2, 100, 0, 2);
insert into stockpile values (3, 100, 0, 3);


insert into specification values (1, '标签1', 1, '图书');
insert into specification values (2, '标签1', 2, '图书');
insert into specification values (3, '标签1', 3, '图书');


# 广告牌
INSERT INTO advertisement VALUES (1, '/static/carousel/jvm3.png', 1);
INSERT INTO advertisement VALUES (2, '/static/carousel/jvm3.png', 2);
INSERT INTO advertisement VALUES (3, '/static/carousel/jvm3.png', 3);


