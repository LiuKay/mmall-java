CREATE DATABASE IF NOT EXISTS mmall;
ALTER DATABASE mmall
    DEFAULT CHARACTER SET utf8
    DEFAULT COLLATE utf8_general_ci;
GRANT ALL PRIVILEGES ON mmall.* TO 'mmall@%' IDENTIFIED BY 'mmall';

DROP TABLE IF EXISTS address;
DROP TABLE IF EXISTS wallet;
DROP TABLE IF EXISTS specification;
DROP TABLE IF EXISTS advertisement;
DROP TABLE IF EXISTS stockpile;
DROP TABLE IF EXISTS payment;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS account;

CREATE TABLE IF NOT EXISTS account
(
    id        int unsigned not null auto_increment primary key,
    username  varchar(50),
    password  varchar(100),
    name      varchar(50),
    avatar    varchar(100),
    telephone varchar(20),
    email     varchar(100),
    location  varchar(100)
) engine = InnoDB;

create table address
(
    id            int unsigned not null auto_increment primary key,
    city          varchar(50)  not null,
    details       varchar(255) null,
    district      varchar(255) null,
    phone         varchar(255) not null,
    province      varchar(50)  not null,
    receiver_name varchar(50)  null,
    user_id       int          not null,
    zip           varchar(255) null
) engine = InnoDB;

create table advertisement
(
    id         int unsigned not null auto_increment primary key,
    image      varchar(255) null,
    product_id int unsigned not null
) engine = InnoDB;

create table payment
(
    id           int unsigned   not null auto_increment primary key,
    create_time  datetime       not null,
    expires      bigint         null,
    pay_id       varchar(255)   not null,
    pay_state    int            null,
    payment_link varchar(255)   null,
    total_price  decimal(19, 2) null
) engine = InnoDB;

create table specification
(
    id         int unsigned not null auto_increment primary key,
    item      varchar(255) not null ,
    product_id int unsigned not null,
    value      varchar(255) null
) engine = InnoDB;

create table product
(
    id          int unsigned   not null auto_increment primary key,
    title       varchar(255)   null,
    price       decimal(19, 2) null,
    description VARCHAR(8000) null,
    cover       varchar(255)   null,
    detail      varchar(255)   null
) engine = InnoDB;

create table stockpile
(
    id         int unsigned not null auto_increment primary key,
    amount     int          default 0,
    frozen     int          default 0,
    product_id int unsigned not null
) engine = InnoDB;

create table wallet
(
    id         int unsigned   not null auto_increment primary key,
    money      decimal(19, 2) null,
    account_id int unsigned   not null
) engine = InnoDB;

create index idx_username on account (username);
create index idx_product_id on specification (product_id);
create index idx_account_id on wallet (account_id);