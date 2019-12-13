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

 Date: 12/12/2019 15:46:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for role_permissions
-- ----------------------------
DROP TABLE IF EXISTS `role_permissions`;
CREATE TABLE `role_permissions`  (
  `id` int(11) NOT NULL COMMENT '角色权限对应关系id',
  `uuid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色权限对应关系uuid',
  `role_uuid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色uuid',
  `permission_uuid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限uuid',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_permissions
-- ----------------------------
INSERT INTO `role_permissions` VALUES (1, '5c3a87b4-1cb3-11ea-b42f-0242ac110003', 'b977008f-1caf-11ea-b42f-0242ac110003', '44fdcfa4-1cb1-11ea-b42f-0242ac110003', '2018-12-14 09:45:37');
INSERT INTO `role_permissions` VALUES (2, '5c3b13e5-1cb3-11ea-b42f-0242ac110003', 'b977008f-1caf-11ea-b42f-0242ac110003', '44fe04bb-1cb1-11ea-b42f-0242ac110003', '2018-12-14 09:45:37');
INSERT INTO `role_permissions` VALUES (3, '5c3b6004-1cb3-11ea-b42f-0242ac110003', 'b977a0ff-1caf-11ea-b42f-0242ac110003', '44fdcfa4-1cb1-11ea-b42f-0242ac110003', '2018-12-14 09:45:37');
INSERT INTO `role_permissions` VALUES (4, '5c3b9498-1cb3-11ea-b42f-0242ac110003', 'b977a0ff-1caf-11ea-b42f-0242ac110003', '44fe402a-1cb1-11ea-b42f-0242ac110003', '2018-12-14 09:45:37');

SET FOREIGN_KEY_CHECKS = 1;
