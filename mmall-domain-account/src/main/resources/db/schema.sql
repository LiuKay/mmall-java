DROP TABLE IF EXISTS address;
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

create index idx_username on account (username);