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
  alter table students modify `avatar_url` varchar(512) COMMENT '用户头像';
  
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
  alter table teachers modify `avatar_url` varchar(512) COMMENT '用户头像';
 
  CREATE TABLE courses(
  `id`  int(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `teacher_id` int(10) UNSIGNED NOT NULL COMMENT '老师id',
  `name` varchar(128) COMMENT '课程名',
  `score` double COMMENT '课程总评分',
  `pages` varchar(1024) COMMENT '课程目录json字符串',
  `note` varchar(512) COMMENT '课程公告',
  `avatar_url` varchar(256) COMMENT '课程图片url',
  `start_time` datetime COMMENT '课程开始时间', 
  `create_time` datetime NOT NULL COMMENT '注册时间',
  `weeks` int(10) UNSIGNED COMMENT '上课总周数',
  `description` varchar(1024) COMMENT '课程介绍',
  `status` int(10) UNSIGNED DEFAULT 1 COMMENT '课程状态 0为正在审核 1为审核通过 2为审核不通过'
  ) ENGINE=InnoDB AUTO_INCREMENT=1102 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
  alter table courses modify `score` double DEFAULT 0 COMMENT '课程总评分';
  alter table courses add `course_num` varchar(64) UNIQUE KEY COMMENT '课程编号';
  alter table courses modify `pages` varchar(2048) COMMENT '课程目录json字符串';
  CREATE TABLE course_student(
  `id`  int(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `course_id`   int(10) UNSIGNED NOT NULL,
  `student_id`  int(10) UNSIGNED NOT NULL,
  UNIQUE KEY (`course_id`, `student_id`)
  ) ENGINE=InnoDB AUTO_INCREMENT=1102 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
  alter table course_student add `create_time` datetime;
  
  CREATE TABLE course_student_remarks(
  `id`  int(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `course_id`   int(10) UNSIGNED NOT NULL,
  `student_id`  int(10) UNSIGNED NOT NULL,
  `score` double NOT NULL COMMENT '打分',
  `remark` varchar(512) COMMENT '评价',
  UNIQUE KEY (`course_id`, `student_id`)
  ) ENGINE=InnoDB AUTO_INCREMENT=1102 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

  CREATE TABLE admins(
  `id`  int(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `user_name` varchar(32) NOT NULL COMMENT '用户名',
  `session_key` varchar(32) NOT NULL DEFAULT '' COMMENT '由密码加密而来的sessionKey',
  `phone` varchar(32) COMMENT '电话',
  `email` varchar(64) COMMENT '邮箱',
  `create_time` datetime NOT NULL COMMENT '注册时间',
  UNIQUE KEY `user_name` (`user_name`)
  ) ENGINE=InnoDB AUTO_INCREMENT=1102 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
  
  CREATE TABLE course_materials(
  `id`  int(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `video_url` varchar(256) COMMENT '课程视频url'
  ) ENGINE=InnoDB AUTO_INCREMENT=1102 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
  alter table course_materials add `type` varchar(255);
  alter table course_materials change `video_url` `material_url` varchar(255);
  
  CREATE TABLE subcourses(
  `id`  int(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `course_id` int(10) UNSIGNED NOT NULL COMMENT '主课程id',
  `title` varchar(64) COMMENT '子课程标题',
  UNIQUE KEY(`course_id`, `title`)
  ) ENGINE=InnoDB AUTO_INCREMENT=1102 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
  
  CREATE TABLE subcourse_to_materials(
  `id`  int(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `material_id` int(10) UNSIGNED NOT NULL COMMENT '课程材料id',
  `subcourse_id` int(10) UNSIGNED NOT NULL COMMENT '子课程id',
  UNIQUE KEY(`material_id`, `subcourse_id`)
  ) ENGINE=InnoDB AUTO_INCREMENT=1102 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
  