drop database blog_management_system;

create database blog_management_system;

use blog_management_system;

create table `user`
(
    `username`      varchar(10) primary key                        not null comment '用户名',
    `password`      varchar(10)                                    not null comment '密码',
    `nickname`      varchar(6)  default '用户昵称'                     not null comment '昵称',
    `avatar`        varchar(50) default '/img/cover.ac211716.webp' not null comment '头像',
    `register_time` datetime    default current_timestamp          not null comment '注册时间',
    `disable_time`  datetime    default current_timestamp          not null comment '封号时长',
    `identity`      tinyint(1)  default 0                          not null comment '身份，0为普通用户，1为管理员',
    `logic_delete`  tinyint(1)  default 0                          not null comment '逻辑删除，1为删除'
) comment '用户账号信息';

insert into `user`(`username`, `password`, `identity`)
values ('admin', 'admin', 1),
       ('user', 'user', 0);


create table `blog`
(
    `id`              int auto_increment primary key       not null comment 'id',
    `title`           varchar(50)                          not null comment '标题',
    `description`     varchar(200) comment '描述',
    `cover`           text comment '特色图片',
    `content`         text                                 not null comment '内容',
    `up`              int        default 0                 not null comment '顶',
    `down`            int        default 0                 not null comment '踩',
    `star`            int        default 0                 not null comment '收藏数',
    `views`           int        default 0                 not null comment '浏览量',
    `author_username` varchar(10)                          not null comment '作者用户名',
    `post_time`       datetime   default current_timestamp not null comment '发布时间',
    `status`          tinyint(1) default 1 comment '发布状态，null为审核中，0为未通过审核，1为通过审核',
    `logic_delete`    tinyint(1) default 0                 not null comment '逻辑删除，1为删除，即审核不通过',
    foreign key (`author_username`) references `user` (`username`)
) comment '博客信息';
# 审核中               status = null and logic_delete = 0;
# 审核通过              status = 1 and logic_delete = 0;
# 审核不通过            status = 0 and logic_delete = 0;
# 删除的审核中的博客       status = null and logic_delete = 1;
# 删除的审核通过的博客     status = 1 and logic_delete = 1;
# 删除的未审核通过的博客    status = 0 and logic_delete = 1;

insert into `blog`(`title`, `content`, `author_username`, `status`)
values ('我的博客1', 'my blog', 'admin', 0),
       ('我的博客2', 'my blog', 'admin', 0),
       ('我的博客3', 'my blog', 'user', 0),
       ('我的博客4', 'my blog', 'user', 0),
       ('我的博客5', 'my blog', 'user', 0),
       ('我的博客6', 'my blog', 'admin', null),
       ('我的博客7', 'my blog', 'admin', null),
       ('我的博客8', 'my blog', 'user', null),
       ('我的博客9', 'my blog', 'user', null),
       ('我的博客11', 'my blog', 'user', null),
       ('我的博客12', 'my blog', 'admin', 1),
       ('我的博客13', 'my blog', 'admin', 1),
       ('我的博客14', 'my blog', 'user', 1),
       ('我的博客15', 'my blog', 'user', 1);



create table `blog_star`
(
    `username`     varchar(10)          not null comment '用户名',
    `blog_id`      int                  not null comment '博客id',
    `logic_delete` tinyint(1) default 0 not null comment '逻辑删除，1为删除',
    foreign key (`username`) references `user` (`username`),
    foreign key (`blog_id`) references `blog` (`id`)
) comment '博客-收藏关系';

insert into `blog_star` (username, blog_id)
values ('user', 1),
       ('user', 2),
       ('user', 5),
       ('user', 6),
       ('user', 4),
       ('admin', 1),
       ('admin', 2),
       ('admin', 4),
       ('admin', 10),
       ('admin', 3),
       ('admin', 6),
       ('user', 10),
       ('user', 3);



create table `blog_up`
(
    `username`     varchar(10)          not null comment '用户名',
    `blog_id`      int                  not null comment '博客id',
    `logic_delete` tinyint(1) default 0 not null comment '逻辑删除，1为删除',
    foreign key (`username`) references `user` (`username`),
    foreign key (`blog_id`) references `blog` (`id`)
) comment '博客-顶关系';

insert into `blog_up` (username, blog_id)
values ('admin', 1),
       ('user', 2),
       ('admin', 3),
       ('user', 3),
       ('user', 10),
       ('user', 5),
       ('admin', 4),
       ('admin', 5),
       ('admin', 6),
       ('admin', 7);



create table `blog_down`
(
    `username`     varchar(10)          not null comment '用户名',
    `blog_id`      int                  not null comment '博客id',
    `logic_delete` tinyint(1) default 0 not null comment '逻辑删除，1为删除',
    foreign key (`username`) references `user` (`username`),
    foreign key (`blog_id`) references `blog` (`id`)
) comment '博客-踩关系';

insert into `blog_down` (username, blog_id)
values ('admin', 1),
       ('user', 2),
       ('admin', 3),
       ('user', 3),
       ('user', 10),
       ('user', 5),
       ('admin', 4),
       ('admin', 5),
       ('admin', 6),
       ('admin', 7);


create table `comments`
(
    `id`              int auto_increment primary key       not null comment 'id',
    `blog_id`         int                                  not null comment '所属博客id',
    `parent`          int comment '父级评论，null则为一级评论，!null则为二级评论',
    `author_username` varchar(10)                          not null comment '评论者的username',
    `content`         varchar(100)                         not null comment '评论内容',
    `up`              int        default 0                 not null comment '顶',
    `down`            int        default 0                 not null comment '踩',
    `post_time`       datetime   default current_timestamp not null comment '发布时间',
    `logic_delete`    tinyint(1) default 0                 not null comment '逻辑删除，1为删除',
    foreign key (`blog_id`) references `blog` (`id`),
    foreign key (`author_username`) references `user` (`username`)
) comment '评论信息';

insert into `comments` (`blog_id`, `parent`, `author_username`, `content`, `up`)
values (1, null, 'user', '写的真不错！', 2),
       (1, 1, 'user', '写的真不错22！', 10),
       (1, null, 'user', '写的真不错23！', 9),
       (1, 1, 'user', '写的真不错24！', 22),
       (1, 1, 'user', '写的真不错25！', 0),
       (1, null, 'user', '写的真不错26！', 0),
       (1, null, 'user', '写的真不错26！', 0),
       (1, 2, 'admin', '写的真不错26！', 0),
       (2, 3, 'admin', '写的真不错26！', 11),
       (2, 4, 'admin', '写的真不错26！', 0),
       (2, 1, 'admin', '写的真不错26！', 0),
       (2, null, 'admin', '写的真不错26！', 70),
       (2, 1, 'admin', '写的真不错26！', 0),
       (2, null, 'admin', '写的真不错27！', 23),
       (2, null, 'admin', '写的真不错3！', 0);

create table `comments_up`
(
    `username`     varchar(10)          not null comment '用户名',
    `comments_id`  int                  not null comment '评论id',
    `logic_delete` tinyint(1) default 0 not null comment '逻辑删除，1为删除',
    foreign key (`username`) references `user` (`username`),
    foreign key (`comments_id`) references `comments` (`id`)
) comment '评论-顶关系';

insert into `comments_up` (username, comments_id)
values ('admin', 1),
       ('user', 2),
       ('admin', 3),
       ('user', 3),
       ('user', 10),
       ('user', 5),
       ('admin', 4),
       ('admin', 5),
       ('admin', 6),
       ('admin', 7);



create table `comments_down`
(
    `username`     varchar(10)          not null comment '用户名',
    `comments_id`  int                  not null comment '评论id',
    `logic_delete` tinyint(1) default 0 not null comment '逻辑删除，1为删除',
    foreign key (`username`) references `user` (`username`),
    foreign key (`comments_id`) references `comments` (`id`)
) comment '评论-踩关系';

insert into `comments_up` (username, comments_id)
values ('admin', 1),
       ('user', 2),
       ('admin', 3),
       ('user', 3),
       ('user', 10),
       ('user', 5),
       ('admin', 4),
       ('admin', 5),
       ('admin', 6),
       ('admin', 7);



create table `category`
(
    `name`         varchar(10) primary key not null comment '名称',
    `description`  varchar(100) comment '描述',
    `cover`        text comment '特色图片',
    `parent`       varchar(10) comment '父分类',
    `logic_delete` tinyint(1) default 0    not null comment '逻辑删除，1为删除'
) comment '分类信息';

insert into `category`(`name`, `description`, `cover`)
values ('java', 'java知识分享圣地', '/img/cover.ac211716.webp'),
       ('js', 'java知识分享圣地2', '/img/cover.ac211716.webp'),
       ('html', 'java知识分享圣地3', '/img/cover.ac211716.webp'),
       ('css', 'java知识分享圣地4', '/img/cover.ac211716.webp'),
       ('python', 'java知识分享圣地5', '/img/cover.ac211716.webp'),
       ('php', 'java知识分享圣地6', '/img/cover.ac211716.webp'),
       ('c', 'java知识分享圣地7', '/img/cover.ac211716.webp'),
       ('c++', 'java知识分享圣地8', '/img/cover.ac211716.webp'),
       ('c#', 'java知识分享圣地9', '/img/cover.ac211716.webp');



create table `category_blog`
(
    `category_name` varchar(10)          not null comment '分类名称',
    `blog_id`       int                  not null comment '博客id',
    `logic_delete`  tinyint(1) default 0 not null comment '逻辑删除，1为删除',
    foreign key (`category_name`) references `category` (`name`),
    foreign key (`blog_id`) references `blog` (`id`)
) comment '分类和博客关系';

insert into `category_blog`(`category_name`, `blog_id`)
values ('java', 1),
       ('java', 2),
       ('java', 3),
       ('js', 2),
       ('js', 3),
       ('js', 4),
       ('python', 6);


insert into user(username, password)
values ('非优秀程序员', 'xx'),
       ('程序员依扬', 'xx'),
       ('郭东东', 'xx'),
       ('我是你的超级英雄', 'xx'),
       ('Gaby', 'xx'),
       ('红尘炼心', 'xx'),
       ('Neal_yang', 'xx'),
       ('vortesnail', 'xx'),
       ('ssh_晨曦时梦见兮', 'xx'),
       ('ssssyoki', 'xx'),
       ('CUGGZ', 'xx'),
       ('ConardLi', 'xx'),
       ('伊人a', 'xx'),
       ('子弈', 'xx'),
       ('信心', 'xx'),
       ('JakeZhang', 'xx'),
       ('yeyan1996', 'xx'),
       ('JowayYoung', 'xx'),
       ('愣锤', 'xx');


insert into blog(title, description, cover, content, up, star, views, author_username, post_time)
values ('如何用 CSS 中写出超级美丽的阴影效果',
        '「这是我参与11月更文挑战的第7天，活动详情查看：2021最后一次更文挑战」。 在我看来，最好的网站和Web应用程序对它们具有切实的"真实"质量。实现这种质量涉及很多因素，但阴影是一个关键因素。 然而', null,
        '暂时为空', 1115, 39, 865061, '非优秀程序员', '2021-11-25 18:03:46'),
       ('【1 月最新】前端 100 问：能搞懂 80% 的请把简历给我',
        '半年时间，几千人参与，精选大厂前端面试高频 100 题，这就是「壹题」。 在 2019 年 1 月 21 日这天，「壹题」项目正式开始，在这之后每个工作日都会出一道高频面试题，主要涵盖阿里、腾讯、头条、百度、网易等大公司和常见题型。得益于大家热情参与，现在每道题都有很多答案，提…',
        null, '暂时为空', 18273, 345, 581088, '程序员依扬', '2021-05-12 07:17:12'),
       ('中高级前端大厂面试秘籍，为你保驾护航金三银四，直通大厂(上)',
        '当下，正面临着近几年来的最严重的互联网寒冬，听得最多的一句话便是：相见于江湖~🤣。缩减HC、裁员不绝于耳，大家都是人心惶惶，年前如此，年后想必肯定又是一场更为惨烈的江湖厮杀。但博主始终相信，寒冬之中，人才更是尤为珍贵。只要有过硬的操作和装备，在逆风局下，同样也能来一波收割翻盘…',
        'https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2019/2/14/168e9dad26638f40~tplv-t2oaga2asx-image.image',
        '暂时为空', 11359, 489, 622193, '郭东东', '2020-09-23 15:42:28'),
       ('30 道 Vue 面试题，内含详细讲解（涵盖入门到精通，自测 Vue 掌握程度）',
        '本文以前端面试官的角度出发，对 Vue 框架中一些重要的特性、框架的原理以问题的形式进行整理汇总，意在帮助作者及读者自测下 Vue 掌握的程度。本文章节结构以从易到难进行组织，建议读者按章节顺序进行阅读，当然大佬级别的请随意。希望读者读完本文，有一定的启发思考，也能对自己的 V…',
        'https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2019/8/19/16ca75c48e0370f0~tplv-t2oaga2asx-image.image',
        '暂时为空', 14921, 252, 565311, '我是你的超级英雄', '2020-09-14 12:33:51'),
       ('🔥 连八股文都不懂还指望在前端混下去么', '前端八股文还是值得深入了解，系统巩固的基础。扎实的基本功还有利于跳槽涨薪的。小知识，大挑战！努力行动起来吧！', null, '暂时为空', 25551, 510,
        471157, 'Gaby', '2022-06-08 10:43:45'),
       ('你会用ES6，那倒是用啊！',
        '不是标题党，这是一位leader在一次代码评审会对小组成员发出的“怒吼”，原因是在代码评审中发现很多地方还是采用ES5的写法，也不是说用ES5写法不行，会有BUG，只是造成代码量增多，可读性变差而已。', null,
        '暂时为空', 12745, 1389, 343350, '红尘炼心', '2021-11-08 18:03:04'),
       ('一个合格(优秀)的前端都应该阅读这些文章',
        '的确，有些标题党了。起因是微信群里，有哥们问我，你是怎么学习前端的呢？能不能共享一下学习方法。一句话也挺触动我的，我真的不算是什么大佬，对于学习前端知识，我也不能说是掌握了什么捷径。当然，我个人的学习方法这篇文章已经在写了，预计这周末会在我个人公众号发布。而在此之前，我想展(g…',
        'https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2019/7/24/16c24b21d59234c9~tplv-t2oaga2asx-image.image',
        '暂时为空', 17485, 389, 336898, 'Neal_yang', '2020-09-13 20:29:44'),
       ('做了一份前端面试复习计划，保熟～',
        '前言 以前我在掘金上看到面试贴就直接刷掉的，从不会多看一眼，直到去年 9 月份我开始准备面试时，才发现很多面试经验贴特别有用，看这些帖子（我不敢称之为文章，怕被杠）的过程中对我的复习思维形成影响很大，', null,
        '暂时为空', 17766, 358, 339858, 'vortesnail', '2022-02-22 22:50:05'),
       ('写给初中级前端的高级进阶指南', '我曾经一度很迷茫，在学了 Vue、React 的实战开发和应用以后，好像遇到了一些瓶颈，不知道该怎样继续深入下去。相信这也是很多一两年经验的前端工程师所遇到共同问题，', null,
        '暂时为空', 14195, 340, 317798, 'ssh_晨曦时梦见兮', '2021-09-30 11:47:39'),
       ('这一次，彻底弄懂 JavaScript 执行机制',
        '本文的目的就是要保证你彻底弄懂javascript的执行机制，如果读完本文还不懂，可以揍我。 不论你是javascript新手还是老鸟，不论是面试求职，还是日常开发工作，我们经常会遇到这样的情况：给定的几行代码，我们需要知道其输出内容和顺序。因为javascript是一门单线程…',
        'https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2017/11/21/15fdd9dfc3293a5c~tplv-t2oaga2asx-image.image',
        '暂时为空', 7530, 811, 287392, 'ssssyoki', '2020-10-31 23:05:48'),
       ('「2021」高频前端面试题汇总之JavaScript篇（上）', '2021 高频前端面试题汇总之JavaScript篇，前端面试题汇总系列文章的JavaScript篇，长期更新，欢迎收藏、点赞！',
        'https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/062ce66d32804d4eb2262f1a9e99a053~tplv-k3u1fbpfcp-watermark.image',
        '暂时为空', 5646, 96, 374496, 'CUGGZ', '2022-08-22 19:41:40'),
       ('一名【合格】前端工程师的自检清单',
        '前端开发是一个非常特殊的行业，它的历史实际上不是很长，但是知识之繁杂，技术迭代速度之快是其他技术所不能比拟的。 这样是一个非常真实的现状，实际上很多前端开发者都是自学甚至转行过来的，前端入门简单，学习了几个API以后上手做项目也很简单，但是这往往成为了限制自身发展的瓶颈。 只是…',
        'https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2019/4/26/16a5710d2f1ea509~tplv-t2oaga2asx-image.image',
        '暂时为空', 11268, 620, 271793, 'ConardLi', '2021-04-06 21:20:20'),
       ('2021年我的前端面试准备', '本文1.6W字面试准备包含思路以及基础面试题整理，较适用于初、中级前端，另外文末整理了思维导图，可以更加直观的找到你的知识盲区，希望能给你带去些许帮助，助力你找到心仪的好工作。',
        'https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/6c5eaee9d61e441d9ca1e7c0ffd104ca~tplv-k3u1fbpfcp-watermark.image',
        '暂时为空', 11995, 563, 287456, '伊人a', '2022-03-10 13:20:26'),
       ('面试分享：两年工作经验成功面试阿里P6总结',
        '本文主要给大家带来一些我面试的经历和经验，希望对正在求职的同学有所帮助。我先大致说下面试之前的个人情况：2017年7月正式入职海康威视数字技术股份有限公司，使用Vue.js技术栈。 我写的篇幅可能有点长，如果只想看成功的面试请直接从阿里企业智能事业部（一面）开始，大家见谅哈。 …',
        null, '暂时为空', 6258, 444, 294179, '子弈', '2020-09-14 15:24:28'),
       ('2018前端面试总结，看完弄懂，工资少说加3K | 掘金技术征文',
        '文章涉及的内容可能不全面，但量很多，需要慢慢看。来源于各个地方，我花了很长的时间整理，希望对大家有帮助。但是难免会有打字的错误或理解的错误，希望发现的可以邮箱告诉我236490794@qq.com，我会及时的进行修改，旨在能帮到大家，谢谢。 意义：根据内容的结构化（内容语义化）…',
        null, '暂时为空', 5855, 140, 262347, '信心', '2020-09-09 09:46:25'),
       ('字节跳动面试官：请你实现一个大文件上传和断点续传', '这段时间面试官都挺忙的，频频出现在博客文章标题，虽然我不是特别想蹭热度，但是实在想不到好的标题了-。-，蹭蹭就蹭蹭 :)', null, '暂时为空', 7766,
        575, 250583, 'yeyan1996', '2022-05-14 15:56:40'),
       ('由浅入深，66条JavaScript面试知识点',
        '我只想面个CV工程师，面试官偏偏让我挑战造火箭工程师，加上今年这个情况更是前后两男，但再难苟且的生活还要继续，饭碗还是要继续找的。在最近的面试中我一直在总结，每次面试回来也都会复盘，下面是我这几天遇到的面试知识点。但今天主题是标题所写的66条JavaScript知识点，由浅入深…',
        'https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2020/6/29/172fdc3497423a51~tplv-t2oaga2asx-image.image',
        '暂时为空', 8746, 205, 266700, 'JakeZhang', '2020-09-02 17:20:45'),
       ('一个合格的中级前端工程师需要掌握的 28 个 JavaScript 技巧',
        '2. 循环实现数组 map 方法 3. 使用 reduce 实现数组 map 方法 4. 循环实现数组 filter 方法 5. 使用 reduce 实现数组 filter 方法 6. 循环实现数组的', null,
        '暂时为空', 8639, 323, 250739, 'yeyan1996', '2022-06-26 20:50:14'),
       ('1.5万字概括ES6全部特性(已更新ES2020)',
        '第三次阅读阮一峰老师的《ES6标准入门》了，以前阅读时不细心，很多地方都是一目十行。最近这次阅读都是逐个逐个字来读，发现很多以前都没有注意到的知识点，为了方便记忆和预览全部ES6特性，所以写下本文。 本文的知识点完全是参考或摘录《ES6》里的语句，有部分语句为了方便理解和记忆，…',
        'https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/07525f7f55dc492c9e70157f45140e37~tplv-k3u1fbpfcp-watermark.image',
        '暂时为空', 10125, 233, 218510, 'JowayYoung', '2021-02-25 15:30:42'),
       ('vue中Axios的封装和API接口的管理',
        '回归正题，我们所要的说的axios的封装和api接口的统一管理，其实主要目的就是在帮助我们简化代码和利于后期的更新维护。 在vue项目中，和后台交互获取数据这块，我们通常使用的是axios库，它是基于promise的http库，可运行在浏览器端和node.js中。他有很多优秀的…',
        null, '暂时为空', 5746, 335, 233486, '愣锤', '2020-09-08 19:28:26');