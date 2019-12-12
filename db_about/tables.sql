use etd_db;

CREATE TABLE students(
  `id`  int(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `user_name` varchar(32) NOT NULL COMMENT '用户名',
  `session_key` varchar(32) NOT NULL DEFAULT '' COMMENT '由密码加密而来的sessionKey',
  `real_name` varchar(32) COMMENT '用户真实名字',
  `phone` varchar(32) COMMENT '电话',
  `email` varchar(64) COMMENT '邮箱', 
  `create_time` datetime NOT NULL COMMENT '注册时间',
  `avatar_url` varchar(128) COMMENT '用户头像',
  UNIQUE KEY `user_name` (`user_name`) 
  ) ENGINE=InnoDB AUTO_INCREMENT=1102 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
  alter table students add sex varchar(32) COMMENT '性别';
  alter table students add valid boolean default true COMMENT '是否为有效用户';
  
  
 CREATE TABLE teachers(
  `id`  int(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `user_name` varchar(32) NOT NULL COMMENT '用户名',
  `session_key` varchar(32) NOT NULL DEFAULT '' COMMENT '由密码加密而来的sessionKey',
  `real_name` varchar(32) COMMENT '用户真实名字',
  `phone` varchar(32) COMMENT '电话',
  `email` varchar(64) COMMENT '邮箱', 
  `create_time` datetime NOT NULL COMMENT '注册时间',
  `avatar_url` varchar(128) COMMENT '用户头像',
  `description` varchar(1024) COMMENT '介绍',
  UNIQUE KEY `user_name` (`user_name`) 
  ) ENGINE=InnoDB AUTO_INCREMENT=1102 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;