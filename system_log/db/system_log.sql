/*
 Navicat Premium Data Transfer

 Source Server         : vm-mysql
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : 192.168.182.88:13306
 Source Schema         : system_log

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 06/01/2020 15:49:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_logs
-- ----------------------------
DROP TABLE IF EXISTS `sys_logs`;
CREATE TABLE `sys_logs`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `type` int(11) NULL DEFAULT NULL COMMENT '日志记录的类型：\r\n//            1：成功操作；\r\n//            2：失败操作；\r\n//            3：系统错误（严重错误）；\r\n//            4：一般信息；\r\n//            5：异常信息（一般异常）；\r\n//            6：警告；\r\n',
  `caller` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '调用者（其他模块或系统名称）',
  `account_info` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '系统或模块的用户账户信息：\r\naccount_uuid \r\naccount_name \r\naccount_alias \r\naccess_token ',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '日志标题',
  `contents` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '日志内容',
  `extra_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '扩展信息',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '日志创建时间（日志服务端的时间）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_logs
-- ----------------------------
INSERT INTO `sys_logs` VALUES (5, 'c5a5018f-4655-43e7-aee6-ea3573d83e02', 2, 'back-end-server', '123', 'echoErrorCode', 'echoErrorCode', '', '2019-12-06 16:16:08');
INSERT INTO `sys_logs` VALUES (6, 'adee0c17-e7e0-4e87-8b39-c7afeabb0a2a', 2, 'back-end-server', '123', 'echoErrorCode', 'echoErrorCode', '', '2019-12-06 16:16:15');
INSERT INTO `sys_logs` VALUES (32, '42e141cb-8383-4102-9e10-11f13192660f', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '\"system-code is running on port: 10113. Register server is: http://localhost:10100/eureka/\"', '', '2019-12-20 16:37:28');
INSERT INTO `sys_logs` VALUES (33, '689ed592-e2ea-4c93-83e5-92333ea320a4', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '\"system-code is running on port: 10113. Register server is: http://localhost:10100/eureka/\"', '', '2019-12-20 16:38:03');
INSERT INTO `sys_logs` VALUES (88, '28a6f531-2589-448f-a887-b2e3bce525e4', 2, 'uni-auth service', '{\"access_token\":\"\",\"account_uuid\":\"df6cdd4a-1cad-11ea-b42f-0242ac110003\",\"account_name\":\"user_1\",\"account_alias\":\"332\"}', '登录', '账户（user_1）密码校验失败，剩余4次尝试次数（最大尝试次数为5）。', '', '2020-01-06 15:26:27');
INSERT INTO `sys_logs` VALUES (89, '22ef21f3-6560-42ea-a2a9-83939ed5d9c8', 2, 'uni-auth service', '{\"access_token\":\"\",\"account_uuid\":\"faa026ba-1cad-11ea-b42f-0242ac110003\",\"account_name\":\"user_3\",\"account_alias\":\"name3\"}', '登录', '账户（user_3）密码校验失败，剩余4次尝试次数（最大尝试次数为5）。', '', '2020-01-06 15:26:42');
INSERT INTO `sys_logs` VALUES (90, '3cd1763c-0ba3-45d1-802c-f436faa8c1f0', 1, 'uni-auth service', '{\"access_token\":\"1a2c914d-c9f4-401f-88dc-e6bfb60e6d55\",\"account_uuid\":\"faa026ba-1cad-11ea-b42f-0242ac110003\",\"account_name\":\"user_3\",\"account_alias\":\"name3\"}', '登录', '账户（user_3）登录成功', '', '2020-01-06 15:27:00');
INSERT INTO `sys_logs` VALUES (91, 'a914f9be-77be-495e-90b5-e7d491ce1c5e', 2, 'uni-auth service', '{\"access_token\":\"\",\"account_uuid\":\"df6cdd4a-1cad-11ea-b42f-0242ac110003\",\"account_name\":\"user_1\",\"account_alias\":\"332\"}', '登录', '账户（user_1）密码校验失败，剩余3次尝试次数（最大尝试次数为5）。', '', '2020-01-06 15:27:19');
INSERT INTO `sys_logs` VALUES (92, '1eb25705-ad64-42cb-8eb2-15c6e8061d04', 2, 'uni-auth service', '{\"access_token\":\"\",\"account_uuid\":\"df6cdd4a-1cad-11ea-b42f-0242ac110003\",\"account_name\":\"user_1\",\"account_alias\":\"332\"}', '登录', '账户（user_12）不存在！', '', '2020-01-06 15:27:28');
INSERT INTO `sys_logs` VALUES (93, '0a4b08a4-bd0e-4fb5-bd4c-887e6518ca49', 2, 'uni-auth service', '{\"access_token\":\"\",\"account_uuid\":\"df6cdd4a-1cad-11ea-b42f-0242ac110003\",\"account_name\":\"user_1\",\"account_alias\":\"332\"}', '登录', '账户（user_1）密码校验失败，剩余2次尝试次数（最大尝试次数为5）。', '', '2020-01-06 15:47:03');
INSERT INTO `sys_logs` VALUES (94, '07065640-32e1-4c3f-aed8-874af7e4a422', 1, 'uni-auth service', '{\"access_token\":\"1a2c914d-c9f4-401f-88dc-e6bfb60e6d55\",\"account_uuid\":\"faa026ba-1cad-11ea-b42f-0242ac110003\",\"account_name\":\"user_3\",\"account_alias\":\"name3\"}', '登录', '账户（user_3）登录成功', '', '2020-01-06 15:47:15');
INSERT INTO `sys_logs` VALUES (95, '22681285-e6f9-428a-be5a-9ff432234ade', 2, 'uni-auth service', '{\"access_token\":\"\",\"account_uuid\":\"\",\"account_name\":\"user_31\",\"account_alias\":\"\"}', '登录', '账户（user_31）不存在！', '', '2020-01-06 15:47:26');
INSERT INTO `sys_logs` VALUES (96, '465b2b05-ab0c-4c17-bf6c-024a71e2a5a5', 2, 'uni-auth service', '{\"access_token\":\"\",\"account_uuid\":\"df6cdd4a-1cad-11ea-b42f-0242ac110003\",\"account_name\":\"user_1\",\"account_alias\":\"332\"}', '登录', '账户（user_1）密码校验失败，剩余1次尝试次数（最大尝试次数为5）。', '', '2020-01-06 15:47:45');
INSERT INTO `sys_logs` VALUES (97, '889fcb2e-6ad9-4ab8-82dd-a61673ea5897', 2, 'uni-auth service', '{\"access_token\":\"\",\"account_uuid\":\"df6cdd4a-1cad-11ea-b42f-0242ac110003\",\"account_name\":\"user_1\",\"account_alias\":\"332\"}', '登录', '账户（user_1）密码已锁定，请联系系统管理员解锁！', '', '2020-01-06 15:47:50');
INSERT INTO `sys_logs` VALUES (98, '3a5223d8-d877-4856-b937-36b85bee0420', 2, 'uni-auth service', '{\"access_token\":\"\",\"account_uuid\":\"df6cdd4a-1cad-11ea-b42f-0242ac110003\",\"account_name\":\"user_1\",\"account_alias\":\"332\"}', '登录', '账户（user_1）密码已锁定，请联系系统管理员解锁！', '', '2020-01-06 15:47:55');
INSERT INTO `sys_logs` VALUES (99, 'e75f9b7b-dffa-452f-8251-89e0de1a6f39', 2, 'uni-auth service', '{\"access_token\":\"\",\"account_uuid\":\"faa026ba-1cad-11ea-b42f-0242ac110003\",\"account_name\":\"user_3\",\"account_alias\":\"name3\"}', '登录', '账户（user_3）密码校验失败，剩余4次尝试次数（最大尝试次数为5）。', '', '2020-01-06 15:48:04');
INSERT INTO `sys_logs` VALUES (100, '815924b3-ff70-4b90-b8ca-f05156125fc2', 1, 'uni-auth service', '{\"access_token\":\"1a2c914d-c9f4-401f-88dc-e6bfb60e6d55\",\"account_uuid\":\"faa026ba-1cad-11ea-b42f-0242ac110003\",\"account_name\":\"user_3\",\"account_alias\":\"name3\"}', '登录', '账户（user_3）登录成功', '', '2020-01-06 15:48:15');
INSERT INTO `sys_logs` VALUES (101, 'd04bb1a7-19a8-4646-9d05-ce24a94bfdec', 2, 'uni-auth service', '{\"access_token\":\"\",\"account_uuid\":\"df6cdd4a-1cad-11ea-b42f-0242ac110003\",\"account_name\":\"user_1\",\"account_alias\":\"332\"}', '登录', '账户（user_1）密码已锁定，请联系系统管理员解锁！', '', '2020-01-06 15:48:23');

SET FOREIGN_KEY_CHECKS = 1;
