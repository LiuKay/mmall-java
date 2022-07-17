CREATE DATABASE IF NOT EXISTS mmall;
ALTER DATABASE mmall
    DEFAULT CHARACTER SET utf8
    DEFAULT COLLATE utf8_general_ci;
GRANT ALL PRIVILEGES ON mmall.* TO 'mmall@%' IDENTIFIED BY 'mmall';


DROP TABLE IF EXISTS account;
CREATE TABLE IF NOT EXISTS account
(
    id        INTEGER UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username  VARCHAR(50),
    password  VARCHAR(100),
    name      VARCHAR(50),
    avatar    VARCHAR(100),
    telephone VARCHAR(20),
    email     VARCHAR(100),
    location  VARCHAR(100),
    INDEX (username)
) engine = InnoDB;

DROP TABLE IF EXISTS address;
create table address
(
    id            INTEGER UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    city          varchar(50)      not null,
    details       varchar(255)     null,
    district      varchar(255)     null,
    phone         varchar(255)     not null,
    province      varchar(50)      not null,
    receiver_name varchar(50)      null,
    user_id       int              not null,
    zip           varchar(255)     null
) engine = InnoDB;