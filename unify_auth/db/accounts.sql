/*
 Navicat Premium Data Transfer

 Source Server         : vm-mysql
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : 192.168.182.88:13306
 Source Schema         : uni_auth_db

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 12/12/2019 15:08:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for accounts
-- ----------------------------
DROP TABLE IF EXISTS `accounts`;
CREATE TABLE `accounts`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '账号id',
  `uuid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '账号uuid',
  `account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '账号',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '姓名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电子信箱',
  `mobile` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号码',
  `sex` tinyint(1) NULL DEFAULT NULL COMMENT '性别：0-女；1-男',
  `birthday` date NULL DEFAULT NULL COMMENT '出生日期',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_id`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of accounts
-- ----------------------------
INSERT INTO `accounts` VALUES (1, 'df6cdd4a-1cad-11ea-b42f-0242ac110003', 'user_1', 'name1', '12345678', '111@aaa.com', '13901231234', 1, '2011-12-13', '2018-12-13 14:27:38');
INSERT INTO `accounts` VALUES (2, 'f203c47d-1cad-11ea-b42f-0242ac110003', 'user_2', 'name2', '12345678', '111@aaa.com', '13901231234', 0, '2011-12-13', '2018-12-13 14:27:38');
INSERT INTO `accounts` VALUES (3, 'faa026ba-1cad-11ea-b42f-0242ac110003', 'user_3', 'name3', '12345678', '111@aaa.com', '13901231234', 0, '2011-12-13', '2018-12-13 14:27:38');
INSERT INTO `accounts` VALUES (4, '006f7e74-1cae-11ea-b42f-0242ac110003', 'user_4', 'name3', '12345678', '111@aaa.com', '13901231234', 1, '2011-12-13', '2018-12-13 14:27:38');

SET FOREIGN_KEY_CHECKS = 1;
