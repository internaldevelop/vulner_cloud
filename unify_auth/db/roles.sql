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

 Date: 20/12/2019 16:59:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `uuid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色uuid',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色名称',
  `valid` tinyint(1) NULL DEFAULT NULL COMMENT '是否有效：1-有效；0-无效',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES (1, 'b977008f-1caf-11ea-b42f-0242ac110003', 'ROLE_ADMIN', 1, '2018-12-14 09:46:01', '2018-12-14 09:46:03');
INSERT INTO `roles` VALUES (2, 'b977a0ff-1caf-11ea-b42f-0242ac110003', 'ROLE_USER', 1, '2018-12-14 09:46:16', '2018-12-14 09:46:18');
INSERT INTO `roles` VALUES (3, 'b989e0fd-1caf-11ea-b42f-0242ac110003', 'ROLE_AUDIT', 1, '2018-12-14 09:46:16', '2018-12-14 09:46:18');
INSERT INTO `roles` VALUES (4, 'b88910ea-1caf-11ea-b42f-0242ac110003', 'ROLE_GUEST', 1, '2018-12-14 09:46:16', '2018-12-14 09:46:18');

SET FOREIGN_KEY_CHECKS = 1;
