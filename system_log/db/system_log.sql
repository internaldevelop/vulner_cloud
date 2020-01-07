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

 Date: 07/01/2020 16:57:24
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
) ENGINE = InnoDB AUTO_INCREMENT = 102 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

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
INSERT INTO `sys_logs` VALUES (102, '67dbaa36-c43b-4d55-b861-d91c07c22958', 2, 'uni-auth service', '{\"access_token\":\"\",\"account_uuid\":\"\",\"account_name\":\"user_13\",\"account_alias\":\"\"}', '登录', '账户（user_13）不存在！', '', '2020-01-06 15:58:02');
INSERT INTO `sys_logs` VALUES (103, 'ce03576e-a91d-4064-b76b-d14c580dd790', 2, 'uni-auth service', '{\"access_token\":\"\",\"account_uuid\":\"\",\"account_name\":\"user_13\",\"account_alias\":\"\"}', '登录', '账户（user_13）不存在！', '', '2020-01-07 09:18:12');
INSERT INTO `sys_logs` VALUES (104, '6855997b-86f6-4cd4-b615-5734d902a94d', 2, 'uni-auth service', '{\"access_token\":\"\",\"account_uuid\":\"df6cdd4a-1cad-11ea-b42f-0242ac110003\",\"account_name\":\"user_1\",\"account_alias\":\"332\"}', '登录', '账户（user_1）密码已锁定，请联系系统管理员解锁！', '', '2020-01-07 09:19:47');
INSERT INTO `sys_logs` VALUES (105, '434f302a-1253-4c6b-a43e-adf1ad5e549f', 1, 'uni-auth service', '{\"access_token\":\"40ca2a77-9c1a-4254-86e6-b3ccb28b357c\",\"account_uuid\":\"df6cdd4a-1cad-11ea-b42f-0242ac110003\",\"account_name\":\"user_1\",\"account_alias\":\"332\"}', '登录', '账户（user_1）登录成功', '', '2020-01-07 09:20:24');
INSERT INTO `sys_logs` VALUES (106, '7c1a1c49-87a7-46a5-ae67-688ca6789b27', 2, 'uni-auth service', '{\"access_token\":\"\",\"account_uuid\":\"\",\"account_name\":\"user_11\",\"account_alias\":\"\"}', '登录', '账户（user_11）不存在！', '', '2020-01-07 09:41:48');
INSERT INTO `sys_logs` VALUES (107, '3a57d439-6c65-4b7c-a568-5ab11789288c', 2, 'uni-auth service', '{\"access_token\":\"\",\"account_uuid\":\"df6cdd4a-1cad-11ea-b42f-0242ac110003\",\"account_name\":\"user_1\",\"account_alias\":\"332\"}', '登录', '账户（user_1）密码校验失败，剩余4次尝试次数（最大尝试次数为5）。', '', '2020-01-07 09:44:22');
INSERT INTO `sys_logs` VALUES (108, '5cd004dc-6589-4ba3-b12a-b617b49a4cd5', 1, 'uni-auth service', '{\"access_token\":\"fe17eee2-bd72-465c-9187-9a72005dd7d9\",\"account_uuid\":\"df6cdd4a-1cad-11ea-b42f-0242ac110003\",\"account_name\":\"user_1\",\"account_alias\":\"332\"}', '登录', '账户（user_1）登录成功', '', '2020-01-07 09:45:02');
INSERT INTO `sys_logs` VALUES (109, '3e69f7fc-0552-4d7b-bed8-2bc6a570cca7', 1, 'uni-auth service', '{\"access_token\":\"5b1511c7-8b1a-4b03-a732-2292e293fee4\",\"account_uuid\":\"df6cdd4a-1cad-11ea-b42f-0242ac110003\",\"account_name\":\"user_1\",\"account_alias\":\"332\"}', '登录', '账户（user_1）登录成功', '', '2020-01-07 09:46:47');
INSERT INTO `sys_logs` VALUES (110, 'fb8f2864-283d-4586-a784-9d6f613d4181', 1, 'uni-auth service', '{\"access_token\":\"5b1511c7-8b1a-4b03-a732-2292e293fee4\",\"account_uuid\":\"\",\"account_name\":\"\",\"account_alias\":\"\"}', '登出', '账号登出成功', '', '2020-01-07 10:03:51');
INSERT INTO `sys_logs` VALUES (111, '29337be6-44f0-4066-8c45-6163da246d46', 1, 'uni-auth service', '{\"access_token\":\"ba9e6f46-9b76-4a56-8929-80dc60c98301\",\"account_uuid\":\"df6cdd4a-1cad-11ea-b42f-0242ac110003\",\"account_name\":\"user_1\",\"account_alias\":\"332\"}', '登录', '账户（user_1）登录成功', '', '2020-01-07 10:04:51');
INSERT INTO `sys_logs` VALUES (112, '74aa0d14-5bc4-4bbf-89c2-cd0cec45a037', 1, 'uni-auth service', '{\"access_token\":\"ba9e6f46-9b76-4a56-8929-80dc60c98301\",\"account_uuid\":\"df6cdd4a-1cad-11ea-b42f-0242ac110003\",\"account_name\":\"user_1\",\"account_alias\":\"332\"}', '登出', '账号登出成功', '', '2020-01-07 10:05:00');
INSERT INTO `sys_logs` VALUES (113, 'f1b897f7-4790-4d07-aed4-33df60537006', 1, 'uni-auth service', '{\"access_token\":\"2f04a943-ae0d-45a0-85fb-f9dd36559479\",\"account_uuid\":\"df6cdd4a-1cad-11ea-b42f-0242ac110003\",\"account_name\":\"user_1\",\"account_alias\":\"332\"}', '登录', '账户（user_1）登录成功', '', '2020-01-07 10:05:51');
INSERT INTO `sys_logs` VALUES (114, 'a2d4cdc0-1605-4329-96de-7b5fe593baab', 1, 'uni-auth service', '{\"access_token\":\"2f04a943-ae0d-45a0-85fb-f9dd36559479\",\"account_uuid\":\"df6cdd4a-1cad-11ea-b42f-0242ac110003\",\"account_name\":\"user_1\",\"account_alias\":\"332\"}', '登出', '账号登出成功', '', '2020-01-07 10:05:53');
INSERT INTO `sys_logs` VALUES (115, '1a4d9afb-3089-45fa-bbe1-926553d9b7ec', 1, 'uni-auth service', '{\"access_token\":\"329dd9ff-3cce-4807-9a05-6443ae6064ec\",\"account_uuid\":\"df6cdd4a-1cad-11ea-b42f-0242ac110003\",\"account_name\":\"user_1\",\"account_alias\":\"332\"}', '登录', '账户（user_1）登录成功', '', '2020-01-07 10:09:55');
INSERT INTO `sys_logs` VALUES (116, '6a1af403-449d-4833-8453-f922d89333fd', 1, 'uni-auth service', '{\"access_token\":\"329dd9ff-3cce-4807-9a05-6443ae6064ec\",\"account_uuid\":\"df6cdd4a-1cad-11ea-b42f-0242ac110003\",\"account_name\":\"user_1\",\"account_alias\":\"332\"}', '登出', '账号登出成功', '', '2020-01-07 10:11:01');
INSERT INTO `sys_logs` VALUES (117, 'edf6ed4a-5505-4dcc-a9ac-2f7d5e8760bd', 1, 'uni-auth service', '{\"access_token\":\"56e33cfa-e0ad-4d53-8401-6c7a2cc10722\",\"account_uuid\":\"df6cdd4a-1cad-11ea-b42f-0242ac110003\",\"account_name\":\"user_1\",\"account_alias\":\"332\"}', '登录', '账户（user_1）登录成功', '', '2020-01-07 10:11:05');
INSERT INTO `sys_logs` VALUES (118, '3b7fccad-7c34-4b26-9aa0-f8454482cc1a', 1, 'uni-auth service', '{\"access_token\":\"56e33cfa-e0ad-4d53-8401-6c7a2cc10722\",\"account_uuid\":\"\",\"account_name\":\"\",\"account_alias\":\"\"}', '登出', '账号（user_1）登出成功', '', '2020-01-07 10:24:03');
INSERT INTO `sys_logs` VALUES (119, '45060497-464f-4f1a-ae68-d87571b5b19e', 1, 'uni-auth service', '{\"access_token\":\"98ae6061-852a-48d1-a21c-a7c9cb930e8b\",\"account_uuid\":\"df6cdd4a-1cad-11ea-b42f-0242ac110003\",\"account_name\":\"user_1\",\"account_alias\":\"332\"}', '登录', '账号（user_1）登录成功', '', '2020-01-07 10:24:10');
INSERT INTO `sys_logs` VALUES (120, '3ff62361-f92e-4f6b-afa5-924e0f822a76', 1, 'uni-auth service', '{\"access_token\":\"98ae6061-852a-48d1-a21c-a7c9cb930e8b\",\"account_uuid\":\"df6cdd4a-1cad-11ea-b42f-0242ac110003\",\"account_name\":\"user_1\",\"account_alias\":\"332\"}', '登出', '账号（user_1）登出成功', '', '2020-01-07 10:25:04');

SET FOREIGN_KEY_CHECKS = 1;
