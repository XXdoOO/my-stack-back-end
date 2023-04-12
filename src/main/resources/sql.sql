drop
    database blog_management_system;

create
    database blog_management_system;

use
    blog_management_system;

create table `user`
(
    `id`           bigint unsigned auto_increment primary key  not null,
    `email`        varchar(32) unique                          not null comment '邮箱',
    `password`     varchar(16)                                 not null comment '密码',
    `nickname`     varchar(32)         default '用户昵称'          not null comment '昵称',
    `avatar`       varchar(64)         default '/avatar/4.jpg' not null comment '头像',
    `ip`           varchar(16)         default '127.0.0.1'     not null comment 'ip地址',
    `ip_territory` varchar(16)         default '未知'            not null comment 'ip属地',
    `is_admin`     tinyint(1) unsigned default 0               not null comment '身份，0为普通用户，1为管理员'
) comment '用户账号信息';

create table `disable`
(
    `id`       bigint unsigned auto_increment primary key not null,
    `user_id`  bigint unsigned                            not null comment '用户名',
    `end_time` datetime                                   not null comment '封号结束时间',
    `reason`   varchar(128)                               not null comment '封号原因',
    foreign key (`user_id`) references `user` (`id`)
) comment '用户封号记录';


create table `blog`
(
    `id`           bigint unsigned auto_increment primary key not null,
    `title`        varchar(128)                               not null comment '标题',
    `description`  varchar(512)                               not null comment '描述',
    `cover`        varchar(64) comment '特色图片',
    `content`      mediumtext                                 not null comment '内容',
    `view`         bigint unsigned     default 0              not null comment '浏览量',
    `ip`           varchar(16)         default '127.0.0.1'    not null comment 'ip地址',
    `ip_territory` varchar(16)         default '未知'           not null comment 'ip属地',
    `status`       tinyint(2) unsigned default 0 comment '发布状态，0为审核中，1为通过审核，2为未通过审核'
) comment '博客信息';

create table `comment`
(
    `id`           bigint unsigned auto_increment primary key not null,
    `blog_id`      bigint unsigned                            not null comment '所属博客id',
    `parent`       bigint unsigned default 0 comment '父级评论，null则为一级评论，!null则为二级评论',
    `receive_id`   bigint unsigned comment '接收者的id，为null则回复的是根评论',
    `content`      varchar(128)                               not null comment '评论内容',
    `ip`           varchar(16)     default '127.0.0.1'        not null comment 'ip地址',
    `ip_territory` varchar(16)     default '未知'               not null comment 'ip属地',
    foreign key (`blog_id`) references `blog` (`id`),
    foreign key (`receive_id`) references `user` (`id`)
) comment '评论信息';

create table `record`
(
    `id`         bigint unsigned auto_increment primary key not null,
    `blog_id`    bigint unsigned comment '博客id',
    `comment_id` bigint unsigned comment '评论id',
    `type`       int unsigned                               not null comment '记录类型，0为up，1为down，2为star，3为views',
    foreign key (`blog_id`) references `blog` (`id`)
) comment '收藏关系';

create table `category`
(
    `id`   bigint unsigned auto_increment primary key not null,
    `name` varchar(16)                                not null comment '名称'
) comment '分类';

create table `blog_category`
(
    `id`          bigint unsigned auto_increment primary key not null,
    `blog_id`     bigint unsigned                            not null comment '博客id',
    `category_id` bigint unsigned                            not null comment '分类id',
    foreign key (`category_id`) references `category` (`id`)

) comment '博客和分类关系';

create table `dict_type`
(
    `id`   bigint unsigned auto_increment primary key not null,
    `name` varchar(16) unique                         not null comment '字典名称'
) comment '字典类型';

create table `dict_data`
(
    `id`        bigint unsigned auto_increment primary key not null,
    `dict_name` varchar(16)                                not null comment '字典名称',
    `label`     varchar(8)                                 not null comment 'value',
    `value`     varchar(8)                                 not null comment 'key',
    foreign key (`dict_name`) references `dict_type` (`name`),
    unique index `dict_data` (`dict_name`, `value`)
) comment '字典数据';

# select concat('alter table ', table_name,
#               ' add column (
# `is_enabled` tinyint(1) unsigned default 1 not null,
# `update_time` datetime default current_timestamp  on update current_timestamp not null,
# `create_time` datetime default current_timestamp not null,
# `create_by` bigint unsigned not null,
# `is_deleted` tinyint(1) unsigned default 0 not null
# );'),
#        concat('alter table ', table_name, ' add constraint ', concat('fk_', table_name, '_create_by'),
#               ' foreign key(create_by) references user(id);')
# from information_schema.TABLES t
# where table_schema = 'blog_management_system'
#   and table_name != 'user';

alter table blog
    add column (
        `is_enabled` tinyint(1) unsigned default 1 not null,
        `update_time` datetime default current_timestamp on update current_timestamp not null,
        `create_time` datetime default current_timestamp not null,
        `create_by` bigint unsigned not null,
        `is_deleted` tinyint(1) unsigned default 0 not null
        );
alter table blog
    add constraint fk_blog_create_by foreign key (create_by) references user (id);
alter table blog_category
    add column (
        `is_enabled` tinyint(1) unsigned default 1 not null,
        `update_time` datetime default current_timestamp on update current_timestamp not null,
        `create_time` datetime default current_timestamp not null,
        `create_by` bigint unsigned not null,
        `is_deleted` tinyint(1) unsigned default 0 not null
        );
alter table blog_category
    add constraint fk_blog_category_create_by foreign key (create_by) references user (id);
alter table category
    add column (
        `is_enabled` tinyint(1) unsigned default 1 not null,
        `update_time` datetime default current_timestamp on update current_timestamp not null,
        `create_time` datetime default current_timestamp not null,
        `create_by` bigint unsigned not null,
        `is_deleted` tinyint(1) unsigned default 0 not null
        );
alter table category
    add constraint fk_category_create_by foreign key (create_by) references user (id);
alter table comment
    add column (
        `is_enabled` tinyint(1) unsigned default 1 not null,
        `update_time` datetime default current_timestamp on update current_timestamp not null,
        `create_time` datetime default current_timestamp not null,
        `create_by` bigint unsigned not null,
        `is_deleted` tinyint(1) unsigned default 0 not null
        );
alter table comment
    add constraint fk_comment_create_by foreign key (create_by) references user (id);
alter table dict_data
    add column (
        `is_enabled` tinyint(1) unsigned default 1 not null,
        `update_time` datetime default current_timestamp on update current_timestamp not null,
        `create_time` datetime default current_timestamp not null,
        `create_by` bigint unsigned not null,
        `is_deleted` tinyint(1) unsigned default 0 not null
        );
alter table dict_data
    add constraint fk_dict_data_create_by foreign key (create_by) references user (id);
alter table dict_type
    add column (
        `is_enabled` tinyint(1) unsigned default 1 not null,
        `update_time` datetime default current_timestamp on update current_timestamp not null,
        `create_time` datetime default current_timestamp not null,
        `create_by` bigint unsigned not null,
        `is_deleted` tinyint(1) unsigned default 0 not null
        );
alter table dict_type
    add constraint fk_dict_type_create_by foreign key (create_by) references user (id);
alter table disable
    add column (
        `is_enabled` tinyint(1) unsigned default 1 not null,
        `update_time` datetime default current_timestamp on update current_timestamp not null,
        `create_time` datetime default current_timestamp not null,
        `create_by` bigint unsigned not null,
        `is_deleted` tinyint(1) unsigned default 0 not null
        );
alter table disable
    add constraint fk_disable_create_by foreign key (create_by) references user (id);
alter table record
    add column (
        `is_enabled` tinyint(1) unsigned default 1 not null,
        `update_time` datetime default current_timestamp on update current_timestamp not null,
        `create_time` datetime default current_timestamp not null,
        `create_by` bigint unsigned not null,
        `is_deleted` tinyint(1) unsigned default 0 not null
        );
alter table record
    add constraint fk_record_create_by foreign key (create_by) references user (id);
alter table user
    add column (
        `is_enabled` tinyint(1) unsigned default 1 not null,
        `update_time` datetime default current_timestamp on update current_timestamp not null,
        `create_time` datetime default current_timestamp not null,
        `is_deleted` tinyint(1) unsigned default 0 not null
        );

666

