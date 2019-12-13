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

 Date: 12/12/2019 15:45:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account_roles
-- ----------------------------
DROP TABLE IF EXISTS `account_roles`;
CREATE TABLE `account_roles`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '账号角色对应关系id',
  `uuid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '账号角色对应关系uuid',
  `account_uuid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '账号uuid',
  `role_uuid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色uuid',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of account_roles
-- ----------------------------
INSERT INTO `account_roles` VALUES (1, '966f4ad9-1cb2-11ea-b42f-0242ac110003', 'df6cdd4a-1cad-11ea-b42f-0242ac110003', 'b977008f-1caf-11ea-b42f-0242ac110003', '2018-12-14 09:45:37');
INSERT INTO `account_roles` VALUES (2, '966fd5f4-1cb2-11ea-b42f-0242ac110003', 'f203c47d-1cad-11ea-b42f-0242ac110003', 'b977008f-1caf-11ea-b42f-0242ac110003', '2018-12-14 09:45:37');
INSERT INTO `account_roles` VALUES (3, '9670615e-1cb2-11ea-b42f-0242ac110003', 'faa026ba-1cad-11ea-b42f-0242ac110003', 'b977a0ff-1caf-11ea-b42f-0242ac110003', '2018-12-14 09:45:37');
INSERT INTO `account_roles` VALUES (4, '9670a597-1cb2-11ea-b42f-0242ac110003', '006f7e74-1cae-11ea-b42f-0242ac110003', 'b977a0ff-1caf-11ea-b42f-0242ac110003', '2018-12-14 09:45:37');

SET FOREIGN_KEY_CHECKS = 1;
