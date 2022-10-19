CREATE DATABASE IF NOT EXISTS mmall;
ALTER DATABASE mmall
    DEFAULT CHARACTER SET utf8
    DEFAULT COLLATE utf8_general_ci;
GRANT ALL PRIVILEGES ON mmall.* TO 'mmall@%' IDENTIFIED BY 'mmall';

DROP TABLE IF EXISTS specification;
DROP TABLE IF EXISTS advertisement;
DROP TABLE IF EXISTS stockpile;
DROP TABLE IF EXISTS product;

create table advertisement
(
    id         int unsigned not null auto_increment primary key,
    image      varchar(255) null,
    product_id int unsigned not null
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

create index idx_product_id on specification (product_id);