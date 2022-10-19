CREATE DATABASE IF NOT EXISTS mmall;
ALTER DATABASE mmall
    DEFAULT CHARACTER SET utf8
    DEFAULT COLLATE utf8_general_ci;
GRANT ALL PRIVILEGES ON mmall.* TO 'mmall@%' IDENTIFIED BY 'mmall';

DROP TABLE IF EXISTS wallet;
DROP TABLE IF EXISTS payment;

create table payment
(
    id           int unsigned   not null auto_increment primary key,
    create_time  datetime       default current_timestamp,
    update_time  datetime       default current_timestamp,
    expires      bigint         null,
    pay_id       varchar(255)   not null,
    pay_state    int            null,
    payment_link varchar(255)   null,
    total_price  decimal(19, 2) null
) engine = InnoDB;

create table wallet
(
    id         int unsigned   not null auto_increment primary key,
    money      decimal(19, 2) null,
    account_id int unsigned   not null
) engine = InnoDB;

create index idx_account_id on wallet (account_id);