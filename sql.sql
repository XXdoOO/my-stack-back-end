create database blog_management_system;

use blog_management_system;

create table user
(
    `username` varchar(10) primary key not null comment '用户名',
    `password` varchar(10)             not null comment '密码',
    `nickname` varchar(6)              not null comment '昵称',
    `avatar`   text                    not null comment '头像',
    `identity` tinyint(1) default 0    not null
);

insert into user
values ('xx', 'xx', 'xx', 'logo.png', 1);