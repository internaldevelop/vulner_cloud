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

 Date: 20/12/2019 16:59:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '权限id',
  `uuid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限uuid',
  `name` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限描述',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permissions
-- ----------------------------
INSERT INTO `permissions` VALUES (1, '44fcf8c1-1cb1-11ea-b42f-0242ac110003', 'query', '查询', '2018-12-14 09:45:35', '2018-12-14 09:45:37');
INSERT INTO `permissions` VALUES (2, '44fd6de6-1cb1-11ea-b42f-0242ac110003', 'search', '搜索', '2018-12-17 13:23:25', '2018-12-17 13:23:27');
INSERT INTO `permissions` VALUES (3, '44fdcfa4-1cb1-11ea-b42f-0242ac110003', 'hello', 'hello', '2018-12-17 13:23:25', '2018-12-17 13:23:25');
INSERT INTO `permissions` VALUES (4, '44fe04bb-1cb1-11ea-b42f-0242ac110003', 'current-user', '当前用户', '2018-12-17 13:23:25', '2018-12-17 13:23:25');
INSERT INTO `permissions` VALUES (5, '44fe402a-1cb1-11ea-b42f-0242ac110003', 'statistics', '统计', '2018-12-17 13:23:25', '2018-12-17 13:23:25');

SET FOREIGN_KEY_CHECKS = 1;
