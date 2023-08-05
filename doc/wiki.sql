/*
 Navicat Premium Data Transfer

 Source Server         : wiki@localhost
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : localhost:3306
 Source Schema         : wiki

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 19/07/2023 19:21:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` bigint(0) NOT NULL COMMENT 'id',
  `parent` bigint(0) NOT NULL DEFAULT 0 COMMENT '父id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `sort` int(0) NULL DEFAULT NULL COMMENT '顺序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (100, 0, '前端开发', 100);
INSERT INTO `category` VALUES (101, 100, 'Vue', 101);
INSERT INTO `category` VALUES (102, 100, 'HTML & CSS', 102);
INSERT INTO `category` VALUES (200, 0, 'Java', 200);
INSERT INTO `category` VALUES (201, 200, '基础应用', 201);
INSERT INTO `category` VALUES (202, 200, '框架应用', 202);
INSERT INTO `category` VALUES (300, 0, 'Python', 300);
INSERT INTO `category` VALUES (301, 300, '基础应用', 301);
INSERT INTO `category` VALUES (302, 300, '进阶方向应用', 302);
INSERT INTO `category` VALUES (400, 0, '数据库', 400);
INSERT INTO `category` VALUES (401, 400, 'MySQL', 401);
INSERT INTO `category` VALUES (500, 0, '其它', 500);
INSERT INTO `category` VALUES (501, 500, '服务器', 501);
INSERT INTO `category` VALUES (502, 500, '开发工具', 502);
INSERT INTO `category` VALUES (503, 500, '热门服务端语言', 503);

-- ----------------------------
-- Table structure for content
-- ----------------------------
DROP TABLE IF EXISTS `content`;
CREATE TABLE `content`  (
  `id` bigint(0) NOT NULL COMMENT '文档id',
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文档内容' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of content
-- ----------------------------
INSERT INTO `content` VALUES (1, '<blockquote><p><span style=\"background-color: rgb(238, 236, 224);\">这是 Ant Design 的Vue实现，开发和服务于企业级后台产品。</span></p></blockquote><p><br></p><h1 id=\"muggs\"><font color=\"#4d80bf\">#111</font></h1><p><strong><font size=\"4\">这是楷体</font></strong><br></p>');
INSERT INTO `content` VALUES (2, '<p>222<br></p>');
INSERT INTO `content` VALUES (3, '<p>333<br></p>');
INSERT INTO `content` VALUES (332927299731197952, '<p>333<br></p>');
INSERT INTO `content` VALUES (333203204626059264, '<blockquote><p>44<br></p></blockquote>');
INSERT INTO `content` VALUES (334998265152016384, '<blockquote><p>#vue1<br></p></blockquote>');
INSERT INTO `content` VALUES (334998913461391360, '<blockquote><p>vue2<br></p></blockquote>');
INSERT INTO `content` VALUES (335000198248009728, '<blockquote><p>vue3<br></p></blockquote>');
INSERT INTO `content` VALUES (335055921250177024, '<blockquote><p>python</p></blockquote><h1 id=\"0egm8\"><img class=\"eleImg\" style=\"\" src=\"http://img.t.sinajs.cn/t4/appstyle/expression/ext/normal/50/pcmoren_huaixiao_org.png\" alt=\"[[坏笑]]\"></h1><img src=\"https://i.postimg.cc/gJWGSrvG/QQ-20230716231253.png\" style=\"max-width:100%;\">');
INSERT INTO `content` VALUES (335342439554486272, '<blockquote><p>第五<br></p></blockquote>');
INSERT INTO `content` VALUES (335345874022567936, '<blockquote><p>第六<br></p></blockquote>');
INSERT INTO `content` VALUES (335802966118371328, '<blockquote><p>oracle<br></p></blockquote>');
INSERT INTO `content` VALUES (336418389998309376, '<blockquote><p>mysql<br></p></blockquote>');
INSERT INTO `content` VALUES (336418526241886208, '<blockquote><p>html<br></p></blockquote>');
INSERT INTO `content` VALUES (336418852655206400, '<blockquote><p>vue<br></p></blockquote>');

-- ----------------------------
-- Table structure for demo
-- ----------------------------
DROP TABLE IF EXISTS `demo`;
CREATE TABLE `demo`  (
  `id` bigint(0) NOT NULL COMMENT 'id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '测试' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of demo
-- ----------------------------
INSERT INTO `demo` VALUES (1, '测试');

-- ----------------------------
-- Table structure for doc
-- ----------------------------
DROP TABLE IF EXISTS `doc`;
CREATE TABLE `doc`  (
  `id` bigint(0) NOT NULL COMMENT 'id',
  `ebook_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '电子书id',
  `parent` bigint(0) NOT NULL DEFAULT 0 COMMENT '父id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `sort` int(0) NULL DEFAULT NULL COMMENT '顺序',
  `view_count` int(0) NULL DEFAULT 0 COMMENT '阅读数',
  `vote_count` int(0) NULL DEFAULT 0 COMMENT '点赞数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文档' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of doc
-- ----------------------------
INSERT INTO `doc` VALUES (1, 1, 0, '文档1', 1, 110, 4);
INSERT INTO `doc` VALUES (2, 1, 1, '文档1.1', 1, 34, 2);
INSERT INTO `doc` VALUES (3, 1, 0, '文档2', 2, 6, 1);
INSERT INTO `doc` VALUES (4, 1, 3, '文档2.1', 1, 6, 1);
INSERT INTO `doc` VALUES (5, 1, 3, '文档2.2', 2, 7, 1);
INSERT INTO `doc` VALUES (6, 1, 5, '文档2.2.1', 1, 6, 1);
INSERT INTO `doc` VALUES (332927299731197952, 1, 0, '3', 3, 5, 1);
INSERT INTO `doc` VALUES (333203204626059264, 1, 0, '4', 4, 5, 1);
INSERT INTO `doc` VALUES (334998913461391360, 2, 0, 'vue2', 1, 22, 3);
INSERT INTO `doc` VALUES (335000198248009728, 2, 0, 'vue3', 2, 18, 3);
INSERT INTO `doc` VALUES (335055921250177024, 3, 0, 'p1', 1, 8, 3);
INSERT INTO `doc` VALUES (335342439554486272, 1, 0, '5', 5, 5, 1);
INSERT INTO `doc` VALUES (335345874022567936, 1, 0, '6', 6, 4, 6);
INSERT INTO `doc` VALUES (335802966118371328, 5, 0, 'o1', 1, 1, 1);
INSERT INTO `doc` VALUES (336418389998309376, 4, 0, 'mysql1', 1, 2, 1);
INSERT INTO `doc` VALUES (336418526241886208, 331369516514807808, 0, 'html', 1, 1, 1);
INSERT INTO `doc` VALUES (336418852655206400, 331443925195821056, 0, 'vue', 1, 1, 1);

-- ----------------------------
-- Table structure for ebook
-- ----------------------------
DROP TABLE IF EXISTS `ebook`;
CREATE TABLE `ebook`  (
  `id` bigint(0) NOT NULL COMMENT 'id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `category1_id` bigint(0) NULL DEFAULT NULL COMMENT '分类1',
  `category2_id` bigint(0) NULL DEFAULT NULL COMMENT '分类2',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `cover` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面',
  `doc_count` int(0) NOT NULL DEFAULT 0 COMMENT '文档数',
  `view_count` int(0) NOT NULL DEFAULT 0 COMMENT '阅读数',
  `vote_count` int(0) NOT NULL DEFAULT 0 COMMENT '点赞数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '电子书' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ebook
-- ----------------------------
INSERT INTO `ebook` VALUES (1, 'Spring Boot 入门教程', 200, 202, '零基础入门 Java 开发，企业级应用开发最佳首选框架', '/image/cover1.png', 10, 188, 19);
INSERT INTO `ebook` VALUES (2, 'Vue 入门教程', 100, 101, '零基础入门 Vue 开发，企业级应用开发最佳首选框架', '/image/cover2.png', 2, 40, 6);
INSERT INTO `ebook` VALUES (3, 'Python 入门教程', 300, 301, '零基础入门 Python 开发，企业级应用开发最佳首选框架', NULL, 1, 8, 3);
INSERT INTO `ebook` VALUES (4, 'Mysql 入门教程', 400, 401, '零基础入门 Mysql 开发，企业级应用开发最佳首选框架', NULL, 1, 2, 1);
INSERT INTO `ebook` VALUES (5, 'Oracle 入门教程', 500, 502, '零基础入门 Oracle 开发，企业级应用开发最佳首选框架', NULL, 1, 1, 1);
INSERT INTO `ebook` VALUES (331369516514807808, 'html', 100, 102, '2', '/image/cover2.png', 1, 1, 1);
INSERT INTO `ebook` VALUES (331443925195821056, 'vue2', 100, 101, '3', '3', 1, 1, 1);

-- ----------------------------
-- Table structure for ebook_snapshot
-- ----------------------------
DROP TABLE IF EXISTS `ebook_snapshot`;
CREATE TABLE `ebook_snapshot`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `ebook_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '电子书id',
  `date` date NOT NULL COMMENT '快照日期',
  `view_count` int(0) NOT NULL DEFAULT 0 COMMENT '阅读数',
  `vote_count` int(0) NOT NULL DEFAULT 0 COMMENT '点赞数',
  `view_increase` int(0) NOT NULL DEFAULT 0 COMMENT '阅读增长',
  `vote_increase` int(0) NOT NULL DEFAULT 0 COMMENT '点赞增长',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ebook_id_date_unique`(`ebook_id`, `date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 53 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '电子书快照表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ebook_snapshot
-- ----------------------------
INSERT INTO `ebook_snapshot` VALUES (15, 1, '2023-07-15', 147, 18, 81, 3);
INSERT INTO `ebook_snapshot` VALUES (16, 2, '2023-07-15', 6, 2, 6, 2);
INSERT INTO `ebook_snapshot` VALUES (17, 3, '2023-07-15', 2, 1, 2, 1);
INSERT INTO `ebook_snapshot` VALUES (18, 4, '2023-07-15', 0, 0, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (19, 5, '2023-07-15', 0, 0, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (20, 331369516514807808, '2023-07-15', 0, 0, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (21, 331443925195821056, '2023-07-15', 0, 0, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (23, 1, '2023-07-16', 155, 19, 8, 1);
INSERT INTO `ebook_snapshot` VALUES (24, 2, '2023-07-16', 6, 2, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (25, 3, '2023-07-16', 5, 2, 3, 1);
INSERT INTO `ebook_snapshot` VALUES (26, 4, '2023-07-16', 0, 0, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (27, 5, '2023-07-16', 0, 0, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (28, 331369516514807808, '2023-07-16', 0, 0, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (29, 331443925195821056, '2023-07-16', 0, 0, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (32, 1, '2023-07-18', 188, 19, 33, 0);
INSERT INTO `ebook_snapshot` VALUES (33, 2, '2023-07-18', 36, 4, 30, 2);
INSERT INTO `ebook_snapshot` VALUES (34, 3, '2023-07-18', 8, 3, 3, 1);
INSERT INTO `ebook_snapshot` VALUES (35, 4, '2023-07-18', 2, 1, 2, 1);
INSERT INTO `ebook_snapshot` VALUES (36, 5, '2023-07-18', 1, 1, 1, 1);
INSERT INTO `ebook_snapshot` VALUES (37, 331369516514807808, '2023-07-18', 0, 0, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (38, 331443925195821056, '2023-07-18', 1, 1, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (39, 1, '2023-07-17', 155, 19, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (40, 2, '2023-07-17', 6, 2, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (41, 3, '2023-07-17', 5, 2, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (42, 4, '2023-07-17', 0, 0, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (43, 5, '2023-07-17', 0, 0, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (44, 331369516514807808, '2023-07-17', 0, 0, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (45, 331443925195821056, '2023-07-17', 1, 1, 1, 1);
INSERT INTO `ebook_snapshot` VALUES (46, 1, '2023-07-19', 188, 19, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (47, 2, '2023-07-19', 40, 6, 4, 2);
INSERT INTO `ebook_snapshot` VALUES (48, 3, '2023-07-19', 8, 3, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (49, 4, '2023-07-19', 2, 1, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (50, 5, '2023-07-19', 1, 1, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (51, 331369516514807808, '2023-07-19', 1, 1, 1, 1);
INSERT INTO `ebook_snapshot` VALUES (52, 331443925195821056, '2023-07-19', 1, 1, 0, 0);

-- ----------------------------
-- Table structure for test
-- ----------------------------
DROP TABLE IF EXISTS `test`;
CREATE TABLE `test`  (
  `id` bigint(0) NOT NULL COMMENT 'id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '测试' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of test
-- ----------------------------
INSERT INTO `test` VALUES (1, '测试', 'password');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(0) NOT NULL COMMENT 'ID',
  `login_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登陆名',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `password` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `login_name_unique`(`login_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'test', '测试用户', '7354a1d413535a6c0dc5c209e198d799');
INSERT INTO `user` VALUES (333526177220268032, 'test3', 'test4', '7354a1d413535a6c0dc5c209e198d799');
INSERT INTO `user` VALUES (333531748120006656, 'test2', 'test2', '7354a1d413535a6c0dc5c209e198d799');
INSERT INTO `user` VALUES (333558916699000832, 'test5', 'test5nic', 'ed91dd513b0a3dfd3fe0f52948546e65');

SET FOREIGN_KEY_CHECKS = 1;
