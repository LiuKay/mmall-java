create table mmall_cart
(
    id          int auto_increment
        primary key,
    user_id     int      not null,
    product_id  int      null comment '商品id',
    quantity    int      null comment '数量',
    checked     int      null comment '是否选择,1=已勾选,0=未勾选',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '更新时间'
)
    charset = utf8;

create index user_id_index
    on mmall_cart (user_id);

create table mmall_category
(
    id          int auto_increment comment '类别Id'
        primary key,
    parent_id   int                  null comment '父类别id当id=0时说明是根节点,一级类别',
    name        varchar(50)          null comment '类别名称',
    status      tinyint(1) default 1 null comment '类别状态1-正常,2-已废弃',
    sort_order  int(4)               null comment '排序编号,同类展示顺序,数值相等则自然排序',
    create_time datetime             null comment '创建时间',
    update_time datetime             null comment '更新时间'
)
    charset = utf8;

create table mmall_order
(
    id           int auto_increment comment '订单id'
        primary key,
    order_no     bigint         null comment '订单号',
    user_id      int            null comment '用户id',
    shipping_id  int            null,
    payment      decimal(20, 2) null comment '实际付款金额,单位是元,保留两位小数',
    payment_type int(4)         null comment '支付类型,1-在线支付',
    postage      int(10)        null comment '运费,单位是元',
    status       int(10)        null comment '订单状态:0-已取消-10-未付款，20-已付款，40-已发货，50-交易成功，60-交易关闭',
    payment_time datetime       null comment '支付时间',
    send_time    datetime       null comment '发货时间',
    end_time     datetime       null comment '交易完成时间',
    close_time   datetime       null comment '交易关闭时间',
    create_time  datetime       null comment '创建时间',
    update_time  datetime       null comment '更新时间',
    constraint order_no_index
        unique (order_no)
)
    charset = utf8;

create table mmall_order_item
(
    id                 int auto_increment comment '订单子表id'
        primary key,
    user_id            int            null,
    order_no           bigint         null,
    product_id         int            null comment '商品id',
    product_name       varchar(100)   null comment '商品名称',
    product_image      varchar(500)   null comment '商品图片地址',
    current_unit_price decimal(20, 2) null comment '生成订单时的商品单价，单位是元,保留两位小数',
    quantity           int(10)        null comment '商品数量',
    total_price        decimal(20, 2) null comment '商品总价,单位是元,保留两位小数',
    create_time        datetime       null,
    update_time        datetime       null
)
    charset = utf8;

create index order_no_index
    on mmall_order_item (order_no);

create index order_no_user_id_index
    on mmall_order_item (user_id, order_no);

create table mmall_pay_info
(
    id              int auto_increment
        primary key,
    user_id         int          null comment '用户id',
    order_no        bigint       null comment '订单号',
    pay_platform    int(10)      null comment '支付平台:1-支付宝,2-微信',
    platform_number varchar(200) null comment '支付宝支付流水号',
    platform_status varchar(20)  null comment '支付宝支付状态',
    create_time     datetime     null comment '创建时间',
    update_time     datetime     null comment '更新时间'
)
    charset = utf8;

create table mmall_product
(
    id          int auto_increment comment '商品id'
        primary key,
    category_id int              not null comment '分类id,对应mmall_category表的主键',
    name        varchar(100)     not null comment '商品名称',
    subtitle    varchar(200)     null comment '商品副标题',
    main_image  varchar(500)     null comment '产品主图,url相对地址',
    sub_images  text             null comment '图片地址,json格式,扩展用',
    detail      text             null comment '商品详情',
    price       decimal(20, 2)   not null comment '价格,单位-元保留两位小数',
    stock       int              not null comment '库存数量',
    status      int(6) default 1 null comment '商品状态.1-在售 2-下架 3-删除',
    create_time datetime         null comment '创建时间',
    update_time datetime         null comment '更新时间'
)
    charset = utf8;

create table mmall_role
(
    user_id int not null,
    role    int not null
)
    comment 'roles';

create table mmall_shipping
(
    id                int auto_increment
        primary key,
    user_id           int          null comment '用户id',
    receiver_name     varchar(20)  null comment '收货姓名',
    receiver_phone    varchar(20)  null comment '收货固定电话',
    receiver_mobile   varchar(20)  null comment '收货移动电话',
    receiver_province varchar(20)  null comment '省份',
    receiver_city     varchar(20)  null comment '城市',
    receiver_district varchar(20)  null comment '区/县',
    receiver_address  varchar(200) null comment '详细地址',
    receiver_zip      varchar(6)   null comment '邮编',
    create_time       datetime     null,
    update_time       datetime     null
)
    charset = utf8;

create table mmall_user
(
    id          int auto_increment comment '用户表id'
        primary key,
    username    varchar(50)  not null comment '用户名',
    password    varchar(50)  not null comment '用户密码，MD5加密',
    email       varchar(50)  null,
    phone       varchar(20)  null,
    question    varchar(100) null comment '找回密码问题',
    answer      varchar(100) null comment '找回密码答案',
    role        int(4)       not null comment '角色0-管理员,1-普通用户',
    create_time datetime     not null comment '创建时间',
    update_time datetime     not null comment '最后一次更新时间',
    constraint user_name_unique
        unique (username)
)
    charset = utf8;

INSERT INTO mmall.mmall_cart (id, user_id, product_id, quantity, checked, create_time, update_time) VALUES (126, 21, 26, 1, 1, '2017-04-13 21:27:06', '2017-04-13 21:27:06');

INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100001, 0, '家用电器', 1, null, '2017-03-25 16:46:00', '2017-03-25 16:46:00');
INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100002, 0, '数码3C', 1, null, '2017-03-25 16:46:21', '2017-03-25 16:46:21');
INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100003, 0, '服装箱包', 1, null, '2017-03-25 16:49:53', '2017-03-25 16:49:53');
INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100004, 0, '食品生鲜', 1, null, '2017-03-25 16:50:19', '2017-03-25 16:50:19');
INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100005, 0, '酒水饮料', 1, null, '2017-03-25 16:50:29', '2017-03-25 16:50:29');
INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100006, 100001, '冰箱', 1, null, '2017-03-25 16:52:15', '2017-03-25 16:52:15');
INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100007, 100001, '电视', 1, null, '2017-03-25 16:52:26', '2017-03-25 16:52:26');
INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100008, 100001, '洗衣机', 1, null, '2017-03-25 16:52:39', '2017-03-25 16:52:39');
INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100009, 100001, '空调', 1, null, '2017-03-25 16:52:45', '2017-03-25 16:52:45');
INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100010, 100001, '电热水器', 1, null, '2017-03-25 16:52:54', '2017-03-25 16:52:54');
INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100011, 100002, '电脑', 1, null, '2017-03-25 16:53:18', '2017-03-25 16:53:18');
INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100012, 100002, '手机', 1, null, '2017-03-25 16:53:27', '2017-03-25 16:53:27');
INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100013, 100002, '平板电脑', 1, null, '2017-03-25 16:53:35', '2017-03-25 16:53:35');
INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100014, 100002, '数码相机', 1, null, '2017-03-25 16:53:56', '2017-03-25 16:53:56');
INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100015, 100002, '3C配件', 1, null, '2017-03-25 16:54:07', '2017-03-25 16:54:07');
INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100016, 100003, '女装', 1, null, '2017-03-25 16:54:44', '2017-03-25 16:54:44');
INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100017, 100003, '帽子', 1, null, '2017-03-25 16:54:51', '2017-03-25 16:54:51');
INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100018, 100003, '旅行箱', 1, null, '2017-03-25 16:55:02', '2017-03-25 16:55:02');
INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100019, 100003, '手提包', 1, null, '2017-03-25 16:55:09', '2017-03-25 16:55:09');
INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100020, 100003, '保暖内衣', 1, null, '2017-03-25 16:55:18', '2017-03-25 16:55:18');
INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100021, 100004, '零食', 1, null, '2017-03-25 16:55:30', '2017-03-25 16:55:30');
INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100022, 100004, '生鲜', 1, null, '2017-03-25 16:55:37', '2017-03-25 16:55:37');
INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100023, 100004, '半成品菜', 1, null, '2017-03-25 16:55:47', '2017-03-25 16:55:47');
INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100024, 100004, '速冻食品', 1, null, '2017-03-25 16:55:56', '2017-03-25 16:55:56');
INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100025, 100004, '进口食品', 1, null, '2017-03-25 16:56:06', '2017-03-25 16:56:06');
INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100026, 100005, '白酒', 1, null, '2017-03-25 16:56:22', '2017-03-25 16:56:22');
INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100027, 100005, '红酒', 1, null, '2017-03-25 16:56:30', '2017-03-25 16:56:30');
INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100028, 100005, '饮料', 1, null, '2017-03-25 16:56:37', '2017-03-25 16:56:37');
INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100029, 100005, '调制鸡尾酒', 1, null, '2017-03-25 16:56:45', '2017-03-25 16:56:45');
INSERT INTO mmall.mmall_category (id, parent_id, name, status, sort_order, create_time, update_time) VALUES (100030, 100005, '进口洋酒', 1, null, '2017-03-25 16:57:05', '2017-03-25 16:57:05');

INSERT INTO mmall.mmall_order (id, order_no, user_id, shipping_id, payment, payment_type, postage, status, payment_time, send_time, end_time, close_time, create_time, update_time) VALUES (103, 1491753014256, 1, 25, 13998.00, 1, 0, 10, null, null, null, null, '2017-04-09 23:50:14', '2017-04-09 23:50:14');
INSERT INTO mmall.mmall_order (id, order_no, user_id, shipping_id, payment, payment_type, postage, status, payment_time, send_time, end_time, close_time, create_time, update_time) VALUES (104, 1491830695216, 1, 26, 13998.00, 1, 0, 10, null, null, null, null, '2017-04-10 21:24:55', '2017-04-10 21:24:55');
INSERT INTO mmall.mmall_order (id, order_no, user_id, shipping_id, payment, payment_type, postage, status, payment_time, send_time, end_time, close_time, create_time, update_time) VALUES (105, 1492089528889, 1, 29, 3299.00, 1, 0, 10, null, null, null, null, '2017-04-13 21:18:48', '2017-04-13 21:18:48');
INSERT INTO mmall.mmall_order (id, order_no, user_id, shipping_id, payment, payment_type, postage, status, payment_time, send_time, end_time, close_time, create_time, update_time) VALUES (106, 1492090946105, 1, 29, 27894.00, 1, 0, 20, '2017-04-13 21:42:40', null, null, null, '2017-04-13 21:42:26', '2017-04-13 21:42:41');
INSERT INTO mmall.mmall_order (id, order_no, user_id, shipping_id, payment, payment_type, postage, status, payment_time, send_time, end_time, close_time, create_time, update_time) VALUES (107, 1492091003128, 1, 29, 8597.00, 1, 0, 20, '2017-04-13 21:43:38', null, null, null, '2017-04-13 21:43:23', '2017-04-13 21:43:38');
INSERT INTO mmall.mmall_order (id, order_no, user_id, shipping_id, payment, payment_type, postage, status, payment_time, send_time, end_time, close_time, create_time, update_time) VALUES (108, 1492091051313, 1, 29, 1999.00, 1, 0, 10, null, null, null, null, '2017-04-13 21:44:11', '2017-04-13 21:44:11');
INSERT INTO mmall.mmall_order (id, order_no, user_id, shipping_id, payment, payment_type, postage, status, payment_time, send_time, end_time, close_time, create_time, update_time) VALUES (109, 1492091061513, 1, 29, 6598.00, 1, 0, 10, null, null, null, null, '2017-04-13 21:44:21', '2017-04-13 21:44:21');
INSERT INTO mmall.mmall_order (id, order_no, user_id, shipping_id, payment, payment_type, postage, status, payment_time, send_time, end_time, close_time, create_time, update_time) VALUES (110, 1492091069563, 1, 29, 3299.00, 1, 0, 10, null, null, null, null, '2017-04-13 21:44:29', '2017-04-13 21:44:29');
INSERT INTO mmall.mmall_order (id, order_no, user_id, shipping_id, payment, payment_type, postage, status, payment_time, send_time, end_time, close_time, create_time, update_time) VALUES (111, 1492091076073, 1, 29, 4299.00, 1, 0, 10, null, null, null, null, '2017-04-13 21:44:36', '2017-04-13 21:44:36');
INSERT INTO mmall.mmall_order (id, order_no, user_id, shipping_id, payment, payment_type, postage, status, payment_time, send_time, end_time, close_time, create_time, update_time) VALUES (112, 1492091083720, 1, 29, 3299.00, 1, 0, 10, null, null, null, null, '2017-04-13 21:44:43', '2017-04-13 21:44:43');
INSERT INTO mmall.mmall_order (id, order_no, user_id, shipping_id, payment, payment_type, postage, status, payment_time, send_time, end_time, close_time, create_time, update_time) VALUES (113, 1492091089794, 1, 29, 6999.00, 1, 0, 10, null, null, null, null, '2017-04-13 21:44:49', '2017-04-13 21:44:49');
INSERT INTO mmall.mmall_order (id, order_no, user_id, shipping_id, payment, payment_type, postage, status, payment_time, send_time, end_time, close_time, create_time, update_time) VALUES (114, 1492091096400, 1, 29, 6598.00, 1, 0, 10, null, null, null, null, '2017-04-13 21:44:56', '2017-04-13 21:44:56');
INSERT INTO mmall.mmall_order (id, order_no, user_id, shipping_id, payment, payment_type, postage, status, payment_time, send_time, end_time, close_time, create_time, update_time) VALUES (115, 1492091102371, 1, 29, 3299.00, 1, 0, 10, null, null, null, null, '2017-04-13 21:45:02', '2017-04-13 21:45:02');
INSERT INTO mmall.mmall_order (id, order_no, user_id, shipping_id, payment, payment_type, postage, status, payment_time, send_time, end_time, close_time, create_time, update_time) VALUES (116, 1492091110004, 1, 29, 8598.00, 1, 0, 40, '2017-04-13 21:55:16', '2017-04-13 21:55:31', null, null, '2017-04-13 21:45:09', '2017-04-13 21:55:31');
INSERT INTO mmall.mmall_order (id, order_no, user_id, shipping_id, payment, payment_type, postage, status, payment_time, send_time, end_time, close_time, create_time, update_time) VALUES (117, 1492091141269, 1, 29, 22894.00, 1, 0, 20, '2017-04-13 21:46:06', null, null, null, '2017-04-13 21:45:41', '2017-04-13 21:46:07');

INSERT INTO mmall.mmall_order_item (id, user_id, order_no, product_id, product_name, product_image, current_unit_price, quantity, total_price, create_time, update_time) VALUES (113, 1, 1491753014256, 26, 'Apple iPhone 7 Plus (A1661) 128G 玫瑰金色 移动联通电信4G手机', '241997c4-9e62-4824-b7f0-7425c3c28917.jpeg', 6999.00, 2, 13998.00, '2017-04-09 23:50:14', '2017-04-09 23:50:14');
INSERT INTO mmall.mmall_order_item (id, user_id, order_no, product_id, product_name, product_image, current_unit_price, quantity, total_price, create_time, update_time) VALUES (114, 1, 1491830695216, 26, 'Apple iPhone 7 Plus (A1661) 128G 玫瑰金色 移动联通电信4G手机', '241997c4-9e62-4824-b7f0-7425c3c28917.jpeg', 6999.00, 2, 13998.00, '2017-04-10 21:24:55', '2017-04-10 21:24:55');
INSERT INTO mmall.mmall_order_item (id, user_id, order_no, product_id, product_name, product_image, current_unit_price, quantity, total_price, create_time, update_time) VALUES (115, 1, 1492089528889, 27, 'Midea/美的 BCD-535WKZM(E)冰箱双开门对开门风冷无霜智能电家用', 'ac3e571d-13ce-4fad-89e8-c92c2eccf536.jpeg', 3299.00, 1, 3299.00, '2017-04-13 21:18:48', '2017-04-13 21:18:48');
INSERT INTO mmall.mmall_order_item (id, user_id, order_no, product_id, product_name, product_image, current_unit_price, quantity, total_price, create_time, update_time) VALUES (116, 1, 1492090946105, 29, 'Haier/海尔HJ100-1HU1 10公斤滚筒洗衣机全自动带烘干家用大容量 洗烘一体', '173335a4-5dce-4afd-9f18-a10623724c4e.jpeg', 4299.00, 2, 8598.00, '2017-04-13 21:42:26', '2017-04-13 21:42:26');
INSERT INTO mmall.mmall_order_item (id, user_id, order_no, product_id, product_name, product_image, current_unit_price, quantity, total_price, create_time, update_time) VALUES (117, 1, 1492090946105, 28, '4+64G送手环/Huawei/华为 nova 手机P9/P10plus青春', '0093f5d3-bdb4-4fb0-bec5-5465dfd26363.jpeg', 1999.00, 1, 1999.00, '2017-04-13 21:42:26', '2017-04-13 21:42:26');
INSERT INTO mmall.mmall_order_item (id, user_id, order_no, product_id, product_name, product_image, current_unit_price, quantity, total_price, create_time, update_time) VALUES (118, 1, 1492090946105, 27, 'Midea/美的 BCD-535WKZM(E)冰箱双开门对开门风冷无霜智能电家用', 'ac3e571d-13ce-4fad-89e8-c92c2eccf536.jpeg', 3299.00, 1, 3299.00, '2017-04-13 21:42:26', '2017-04-13 21:42:26');
INSERT INTO mmall.mmall_order_item (id, user_id, order_no, product_id, product_name, product_image, current_unit_price, quantity, total_price, create_time, update_time) VALUES (119, 1, 1492090946105, 26, 'Apple iPhone 7 Plus (A1661) 128G 玫瑰金色 移动联通电信4G手机', '241997c4-9e62-4824-b7f0-7425c3c28917.jpeg', 6999.00, 2, 13998.00, '2017-04-13 21:42:26', '2017-04-13 21:42:26');
INSERT INTO mmall.mmall_order_item (id, user_id, order_no, product_id, product_name, product_image, current_unit_price, quantity, total_price, create_time, update_time) VALUES (120, 1, 1492091003128, 27, 'Midea/美的 BCD-535WKZM(E)冰箱双开门对开门风冷无霜智能电家用', 'ac3e571d-13ce-4fad-89e8-c92c2eccf536.jpeg', 3299.00, 2, 6598.00, '2017-04-13 21:43:23', '2017-04-13 21:43:23');
INSERT INTO mmall.mmall_order_item (id, user_id, order_no, product_id, product_name, product_image, current_unit_price, quantity, total_price, create_time, update_time) VALUES (121, 1, 1492091003128, 28, '4+64G送手环/Huawei/华为 nova 手机P9/P10plus青春', '0093f5d3-bdb4-4fb0-bec5-5465dfd26363.jpeg', 1999.00, 1, 1999.00, '2017-04-13 21:43:23', '2017-04-13 21:43:23');
INSERT INTO mmall.mmall_order_item (id, user_id, order_no, product_id, product_name, product_image, current_unit_price, quantity, total_price, create_time, update_time) VALUES (122, 1, 1492091051313, 28, '4+64G送手环/Huawei/华为 nova 手机P9/P10plus青春', '0093f5d3-bdb4-4fb0-bec5-5465dfd26363.jpeg', 1999.00, 1, 1999.00, '2017-04-13 21:44:11', '2017-04-13 21:44:11');
INSERT INTO mmall.mmall_order_item (id, user_id, order_no, product_id, product_name, product_image, current_unit_price, quantity, total_price, create_time, update_time) VALUES (123, 1, 1492091061513, 27, 'Midea/美的 BCD-535WKZM(E)冰箱双开门对开门风冷无霜智能电家用', 'ac3e571d-13ce-4fad-89e8-c92c2eccf536.jpeg', 3299.00, 2, 6598.00, '2017-04-13 21:44:21', '2017-04-13 21:44:21');
INSERT INTO mmall.mmall_order_item (id, user_id, order_no, product_id, product_name, product_image, current_unit_price, quantity, total_price, create_time, update_time) VALUES (124, 1, 1492091069563, 27, 'Midea/美的 BCD-535WKZM(E)冰箱双开门对开门风冷无霜智能电家用', 'ac3e571d-13ce-4fad-89e8-c92c2eccf536.jpeg', 3299.00, 1, 3299.00, '2017-04-13 21:44:29', '2017-04-13 21:44:29');
INSERT INTO mmall.mmall_order_item (id, user_id, order_no, product_id, product_name, product_image, current_unit_price, quantity, total_price, create_time, update_time) VALUES (125, 1, 1492091076073, 29, 'Haier/海尔HJ100-1HU1 10公斤滚筒洗衣机全自动带烘干家用大容量 洗烘一体', '173335a4-5dce-4afd-9f18-a10623724c4e.jpeg', 4299.00, 1, 4299.00, '2017-04-13 21:44:36', '2017-04-13 21:44:36');
INSERT INTO mmall.mmall_order_item (id, user_id, order_no, product_id, product_name, product_image, current_unit_price, quantity, total_price, create_time, update_time) VALUES (126, 1, 1492091083720, 27, 'Midea/美的 BCD-535WKZM(E)冰箱双开门对开门风冷无霜智能电家用', 'ac3e571d-13ce-4fad-89e8-c92c2eccf536.jpeg', 3299.00, 1, 3299.00, '2017-04-13 21:44:43', '2017-04-13 21:44:43');
INSERT INTO mmall.mmall_order_item (id, user_id, order_no, product_id, product_name, product_image, current_unit_price, quantity, total_price, create_time, update_time) VALUES (127, 1, 1492091089794, 26, 'Apple iPhone 7 Plus (A1661) 128G 玫瑰金色 移动联通电信4G手机', '241997c4-9e62-4824-b7f0-7425c3c28917.jpeg', 6999.00, 1, 6999.00, '2017-04-13 21:44:49', '2017-04-13 21:44:49');
INSERT INTO mmall.mmall_order_item (id, user_id, order_no, product_id, product_name, product_image, current_unit_price, quantity, total_price, create_time, update_time) VALUES (128, 1, 1492091096400, 27, 'Midea/美的 BCD-535WKZM(E)冰箱双开门对开门风冷无霜智能电家用', 'ac3e571d-13ce-4fad-89e8-c92c2eccf536.jpeg', 3299.00, 2, 6598.00, '2017-04-13 21:44:56', '2017-04-13 21:44:56');
INSERT INTO mmall.mmall_order_item (id, user_id, order_no, product_id, product_name, product_image, current_unit_price, quantity, total_price, create_time, update_time) VALUES (129, 1, 1492091102371, 27, 'Midea/美的 BCD-535WKZM(E)冰箱双开门对开门风冷无霜智能电家用', 'ac3e571d-13ce-4fad-89e8-c92c2eccf536.jpeg', 3299.00, 1, 3299.00, '2017-04-13 21:45:02', '2017-04-13 21:45:02');
INSERT INTO mmall.mmall_order_item (id, user_id, order_no, product_id, product_name, product_image, current_unit_price, quantity, total_price, create_time, update_time) VALUES (130, 1, 1492091110004, 29, 'Haier/海尔HJ100-1HU1 10公斤滚筒洗衣机全自动带烘干家用大容量 洗烘一体', '173335a4-5dce-4afd-9f18-a10623724c4e.jpeg', 4299.00, 2, 8598.00, '2017-04-13 21:45:09', '2017-04-13 21:45:09');
INSERT INTO mmall.mmall_order_item (id, user_id, order_no, product_id, product_name, product_image, current_unit_price, quantity, total_price, create_time, update_time) VALUES (131, 1, 1492091141269, 26, 'Apple iPhone 7 Plus (A1661) 128G 玫瑰金色 移动联通电信4G手机', '241997c4-9e62-4824-b7f0-7425c3c28917.jpeg', 6999.00, 1, 6999.00, '2017-04-13 21:45:41', '2017-04-13 21:45:41');
INSERT INTO mmall.mmall_order_item (id, user_id, order_no, product_id, product_name, product_image, current_unit_price, quantity, total_price, create_time, update_time) VALUES (132, 1, 1492091141269, 27, 'Midea/美的 BCD-535WKZM(E)冰箱双开门对开门风冷无霜智能电家用', 'ac3e571d-13ce-4fad-89e8-c92c2eccf536.jpeg', 3299.00, 1, 3299.00, '2017-04-13 21:45:41', '2017-04-13 21:45:41');
INSERT INTO mmall.mmall_order_item (id, user_id, order_no, product_id, product_name, product_image, current_unit_price, quantity, total_price, create_time, update_time) VALUES (133, 1, 1492091141269, 29, 'Haier/海尔HJ100-1HU1 10公斤滚筒洗衣机全自动带烘干家用大容量 洗烘一体', '173335a4-5dce-4afd-9f18-a10623724c4e.jpeg', 4299.00, 2, 8598.00, '2017-04-13 21:45:41', '2017-04-13 21:45:41');
INSERT INTO mmall.mmall_order_item (id, user_id, order_no, product_id, product_name, product_image, current_unit_price, quantity, total_price, create_time, update_time) VALUES (134, 1, 1492091141269, 28, '4+64G送手环/Huawei/华为 nova 手机P9/P10plus青春', '0093f5d3-bdb4-4fb0-bec5-5465dfd26363.jpeg', 1999.00, 2, 3998.00, '2017-04-13 21:45:41', '2017-04-13 21:45:41');

INSERT INTO mmall.mmall_pay_info (id, user_id, order_no, pay_platform, platform_number, platform_status, create_time, update_time) VALUES (53, 1, 1492090946105, 1, '2017041321001004300200116250', 'WAIT_BUYER_PAY', '2017-04-13 21:42:33', '2017-04-13 21:42:33');
INSERT INTO mmall.mmall_pay_info (id, user_id, order_no, pay_platform, platform_number, platform_status, create_time, update_time) VALUES (54, 1, 1492090946105, 1, '2017041321001004300200116250', 'TRADE_SUCCESS', '2017-04-13 21:42:41', '2017-04-13 21:42:41');
INSERT INTO mmall.mmall_pay_info (id, user_id, order_no, pay_platform, platform_number, platform_status, create_time, update_time) VALUES (55, 1, 1492091003128, 1, '2017041321001004300200116251', 'WAIT_BUYER_PAY', '2017-04-13 21:43:31', '2017-04-13 21:43:31');
INSERT INTO mmall.mmall_pay_info (id, user_id, order_no, pay_platform, platform_number, platform_status, create_time, update_time) VALUES (56, 1, 1492091003128, 1, '2017041321001004300200116251', 'TRADE_SUCCESS', '2017-04-13 21:43:38', '2017-04-13 21:43:38');
INSERT INTO mmall.mmall_pay_info (id, user_id, order_no, pay_platform, platform_number, platform_status, create_time, update_time) VALUES (57, 1, 1492091141269, 1, '2017041321001004300200116252', 'WAIT_BUYER_PAY', '2017-04-13 21:45:59', '2017-04-13 21:45:59');
INSERT INTO mmall.mmall_pay_info (id, user_id, order_no, pay_platform, platform_number, platform_status, create_time, update_time) VALUES (58, 1, 1492091141269, 1, '2017041321001004300200116252', 'TRADE_SUCCESS', '2017-04-13 21:46:07', '2017-04-13 21:46:07');
INSERT INTO mmall.mmall_pay_info (id, user_id, order_no, pay_platform, platform_number, platform_status, create_time, update_time) VALUES (59, 1, 1492091110004, 1, '2017041321001004300200116396', 'WAIT_BUYER_PAY', '2017-04-13 21:55:08', '2017-04-13 21:55:08');
INSERT INTO mmall.mmall_pay_info (id, user_id, order_no, pay_platform, platform_number, platform_status, create_time, update_time) VALUES (60, 1, 1492091110004, 1, '2017041321001004300200116396', 'TRADE_SUCCESS', '2017-04-13 21:55:17', '2017-04-13 21:55:17');

INSERT INTO mmall.mmall_product (id, category_id, name, subtitle, main_image, sub_images, detail, price, stock, status, create_time, update_time) VALUES (26, 100002, 'Apple iPhone 7 Plus (A1661) 128G 玫瑰金色 移动联通电信4G手机', 'iPhone 7，现更以红色呈现。', '241997c4-9e62-4824-b7f0-7425c3c28917.jpeg', '241997c4-9e62-4824-b7f0-7425c3c28917.jpeg,b6c56eb0-1748-49a9-98dc-bcc4b9788a54.jpeg,92f17532-1527-4563-aa1d-ed01baa0f7b2.jpeg,3adbe4f7-e374-4533-aa79-cc4a98c529bf.jpeg', '<p><img alt="10000.jpg" src="http://img.happymmall.com/00bce8d4-e9af-4c8d-b205-e6c75c7e252b.jpg" width="790" height="553"><br></p><p><img alt="20000.jpg" src="http://img.happymmall.com/4a70b4b4-01ee-46af-9468-31e67d0995b8.jpg" width="790" height="525"><br></p><p><img alt="30000.jpg" src="http://img.happymmall.com/0570e033-12d7-49b2-88f3-7a5d84157223.jpg" width="790" height="365"><br></p><p><img alt="40000.jpg" src="http://img.happymmall.com/50515c02-3255-44b9-a829-9e141a28c08a.jpg" width="790" height="525"><br></p><p><img alt="50000.jpg" src="http://img.happymmall.com/c138fc56-5843-4287-a029-91cf3732d034.jpg" width="790" height="525"><br></p><p><img alt="60000.jpg" src="http://img.happymmall.com/c92d1f8a-9827-453f-9d37-b10a3287e894.jpg" width="790" height="525"><br></p><p><br></p><p><img alt="TB24p51hgFkpuFjSspnXXb4qFXa-1776456424.jpg" src="http://img.happymmall.com/bb1511fc-3483-471f-80e5-c7c81fa5e1dd.jpg" width="790" height="375"><br></p><p><br></p><p><img alt="shouhou.jpg" src="http://img.happymmall.com/698e6fbe-97ea-478b-8170-008ad24030f7.jpg" width="750" height="150"><br></p><p><img alt="999.jpg" src="http://img.happymmall.com/ee276fe6-5d79-45aa-8393-ba1d210f9c89.jpg" width="790" height="351"><br></p>', 6999.00, 9991, 1, null, '2017-04-13 21:45:41');
INSERT INTO mmall.mmall_product (id, category_id, name, subtitle, main_image, sub_images, detail, price, stock, status, create_time, update_time) VALUES (27, 100006, 'Midea/美的 BCD-535WKZM(E)冰箱双开门对开门风冷无霜智能电家用', '送品牌烤箱，五一大促', 'ac3e571d-13ce-4fad-89e8-c92c2eccf536.jpeg', 'ac3e571d-13ce-4fad-89e8-c92c2eccf536.jpeg,4bb02f1c-62d5-48cc-b358-97b05af5740d.jpeg,36bdb49c-72ae-4185-9297-78829b54b566.jpeg', '<p><img alt="miaoshu.jpg" src="http://img.happymmall.com/9c5c74e6-6615-4aa0-b1fc-c17a1eff6027.jpg" width="790" height="444"><br></p><p><img alt="miaoshu2.jpg" src="http://img.happymmall.com/31dc1a94-f354-48b8-a170-1a1a6de8751b.jpg" width="790" height="1441"><img alt="miaoshu3.jpg" src="http://img.happymmall.com/7862594b-3063-4b52-b7d4-cea980c604e0.jpg" width="790" height="1442"><img alt="miaoshu4.jpg" src="http://img.happymmall.com/9a650563-dc85-44d6-b174-d6960cfb1d6a.jpg" width="790" height="1441"><br></p>', 3299.00, 8876, 1, '2017-04-13 18:51:54', '2017-04-13 21:45:41');
INSERT INTO mmall.mmall_product (id, category_id, name, subtitle, main_image, sub_images, detail, price, stock, status, create_time, update_time) VALUES (28, 100012, '4+64G送手环/Huawei/华为 nova 手机P9/P10plus青春', 'NOVA青春版1999元', '0093f5d3-bdb4-4fb0-bec5-5465dfd26363.jpeg', '0093f5d3-bdb4-4fb0-bec5-5465dfd26363.jpeg,13da2172-4445-4eb5-a13f-c5d4ede8458c.jpeg,58d5d4b7-58d4-4948-81b6-2bae4f79bf02.jpeg', '<p><img alt="11TB2fKK3cl0kpuFjSsziXXa.oVXa_!!1777180618.jpg" src="http://img.happymmall.com/5c2d1c6d-9e09-48ce-bbdb-e833b42ff664.jpg" width="790" height="966"><img alt="22TB2YP3AkEhnpuFjSZFpXXcpuXXa_!!1777180618.jpg" src="http://img.happymmall.com/9a10b877-818f-4a27-b6f7-62887f3fb39d.jpg" width="790" height="1344"><img alt="33TB2Yyshk.hnpuFjSZFpXXcpuXXa_!!1777180618.jpg" src="http://img.happymmall.com/7d7fbd69-a3cb-4efe-8765-423bf8276e3e.jpg" width="790" height="700"><img alt="TB2diyziB8kpuFjSspeXXc7IpXa_!!1777180618.jpg" src="http://img.happymmall.com/1d7160d2-9dba-422f-b2a0-e92847ba6ce9.jpg" width="790" height="393"><br></p>', 1999.00, 9994, 1, '2017-04-13 18:57:18', '2017-04-13 21:45:41');
INSERT INTO mmall.mmall_product (id, category_id, name, subtitle, main_image, sub_images, detail, price, stock, status, create_time, update_time) VALUES (29, 100008, 'Haier/海尔HJ100-1HU1 10公斤滚筒洗衣机全自动带烘干家用大容量 洗烘一体', '门店机型 德邦送货', '173335a4-5dce-4afd-9f18-a10623724c4e.jpeg', '173335a4-5dce-4afd-9f18-a10623724c4e.jpeg,42b1b8bc-27c7-4ee1-80ab-753d216a1d49.jpeg,2f1b3de1-1eb1-4c18-8ca2-518934931bec.jpeg', '<p><img alt="1TB2WLZrcIaK.eBjSspjXXXL.XXa_!!2114960396.jpg" src="http://img.happymmall.com/ffcce953-81bd-463c-acd1-d690b263d6df.jpg" width="790" height="920"><img alt="2TB2zhOFbZCO.eBjSZFzXXaRiVXa_!!2114960396.jpg" src="http://img.happymmall.com/58a7bd25-c3e7-4248-9dba-158ef2a90e70.jpg" width="790" height="1052"><img alt="3TB27mCtb7WM.eBjSZFhXXbdWpXa_!!2114960396.jpg" src="http://img.happymmall.com/2edbe9b3-28be-4a8b-a9c3-82e40703f22f.jpg" width="790" height="820"><br></p>', 4299.00, 9993, 1, '2017-04-13 19:07:47', '2017-04-13 21:45:41');

INSERT INTO mmall.mmall_role (user_id, role) VALUES (22, 0);

INSERT INTO mmall.mmall_shipping (id, user_id, receiver_name, receiver_phone, receiver_mobile, receiver_province, receiver_city, receiver_district, receiver_address, receiver_zip, create_time, update_time) VALUES (4, 13, 'geely', '010', '18688888888', '北京', '北京市', '海淀区', '中关村', '100000', '2017-01-22 14:26:25', '2017-01-22 14:26:25');
INSERT INTO mmall.mmall_shipping (id, user_id, receiver_name, receiver_phone, receiver_mobile, receiver_province, receiver_city, receiver_district, receiver_address, receiver_zip, create_time, update_time) VALUES (7, 17, 'Rosen', '13800138000', '13800138000', '北京', '北京', null, '中关村', '100000', '2017-03-29 12:11:01', '2017-03-29 12:11:01');
INSERT INTO mmall.mmall_shipping (id, user_id, receiver_name, receiver_phone, receiver_mobile, receiver_province, receiver_city, receiver_district, receiver_address, receiver_zip, create_time, update_time) VALUES (29, 1, '吉利', '13800138000', '13800138000', '北京', '北京', '海淀区', '海淀区中关村', '100000', '2017-04-09 18:33:32', '2017-04-09 18:33:32');

INSERT INTO mmall.mmall_user (id, username, password, email, phone, question, answer, role, create_time, update_time) VALUES (1, 'admin', 'B8D37716DF0DF3EE35677E536BF895D3', 'admin@happymmall.com', '13800138001', '问题', '答案', 1, '2016-11-06 16:56:45', '2017-04-04 19:27:36');
INSERT INTO mmall.mmall_user (id, username, password, email, phone, question, answer, role, create_time, update_time) VALUES (13, 'geely', '08E9A6EA287E70E7E3F7C982BF7923AC', 'geely@happymmall.com', '13800138002', '问题', '答案', 0, '2016-11-19 22:19:25', '2016-11-19 22:19:25');
INSERT INTO mmall.mmall_user (id, username, password, email, phone, question, answer, role, create_time, update_time) VALUES (17, 'rosen', '095AC193FE2212EEC7A93E8FEFF11902', 'rosen1@happymmall.com', '13800138003', '问题', '答案', 0, '2017-03-17 10:51:33', '2017-04-09 23:13:26');
INSERT INTO mmall.mmall_user (id, username, password, email, phone, question, answer, role, create_time, update_time) VALUES (21, 'soonerbetter', 'DE6D76FE7C40D5A1A8F04213F2BEFBEE', 'test06@happymmall.com', '13800138004', '105204', '105204', 0, '2017-04-13 21:26:22', '2017-04-13 21:26:22');
INSERT INTO mmall.mmall_user (id, username, password, email, phone, question, answer, role, create_time, update_time) VALUES (22, 'test', '$2a$10$JsHq49JFfYZkKu6Zf4DN0eA1TI7i/ztzfOonft3QxztgFQ9JhNFEu', 'test@123456.com', '18012341234', 'qqq', '111', 0, '2019-05-21 21:55:33', '2019-05-21 21:55:33');