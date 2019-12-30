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

 Date: 30/12/2019 14:04:49
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
  `create_account_uuid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '系统或模块的使用者账户 UUID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '日志标题',
  `contents` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '日志内容',
  `extra_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '扩展信息',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '日志创建时间（日志服务端的时间）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_logs
-- ----------------------------
INSERT INTO `sys_logs` VALUES (1, 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 2, '12333AAA', 'eeeeeeeeeeeeee4324gf', 'tttt', ' rv = systemLogService.addLog(caller, account_uuid, type, title, contents, extra_info);', '', '2019-12-06 16:00:57');
INSERT INTO `sys_logs` VALUES (2, 'a5a5894f-9317-4ffb-84cd-f851faf4916d', 2, '12333AAA', 'eeeeeeeeeeeeee4324gf', 'tttt', ' rv = systemLogService.addLog(caller, account_uuid, type, title, contents, extra_info);', '', '2019-12-06 16:01:22');
INSERT INTO `sys_logs` VALUES (3, '96ae43a1-21b7-49af-aa67-6242ff3e494d', 2, '12333AAA', 'eeeeeeeeeeeeee4324gf', 'tttt', ' rv = systemLogService.addLog(caller, account_uuid, type, title, contents, extra_info);', '', '2019-12-06 16:01:22');
INSERT INTO `sys_logs` VALUES (4, '10eb8034-cf67-4406-a0a2-8ab18c9c6330', 2, '12333AAA', 'eeeeeeeeeeeeee4324gf', 'tttt', ' rv = systemLogService.addLog(caller, account_uuid, type, title, contents, extra_info);', '', '2019-12-06 16:01:23');
INSERT INTO `sys_logs` VALUES (5, 'c5a5018f-4655-43e7-aee6-ea3573d83e02', 2, 'back-end-server', '123', 'echoErrorCode', 'echoErrorCode', '', '2019-12-06 16:16:08');
INSERT INTO `sys_logs` VALUES (6, 'adee0c17-e7e0-4e87-8b39-c7afeabb0a2a', 2, 'back-end-server', '123', 'echoErrorCode', 'echoErrorCode', '', '2019-12-06 16:16:15');
INSERT INTO `sys_logs` VALUES (7, 'bb1881cc-26bd-4cb6-9e13-bc1bc1783783', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '获取状态成功', '', '2019-12-06 17:40:04');
INSERT INTO `sys_logs` VALUES (8, '28aeeed5-bbd3-42c5-9a90-6da262f34e38', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '\"system-code is running on port: 10113. Register server is: http://localhost:10100/eureka/\"', '', '2019-12-06 17:52:42');
INSERT INTO `sys_logs` VALUES (9, '233c0e67-5296-4786-9d4f-0d1dfadc6831', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '\"system-code is running on port: 10113. Register server is: http://localhost:10100/eureka/\"', '', '2019-12-06 17:53:50');
INSERT INTO `sys_logs` VALUES (10, '46d6470a-e1eb-4681-9715-1c689157b42b', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '\"system-code is running on port: 10113. Register server is: http://localhost:10100/eureka/\"', '', '2019-12-09 09:34:04');
INSERT INTO `sys_logs` VALUES (11, '7754e193-a3c9-4043-aebc-7113a9fb12e9', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '\"system-code is running on port: 10113. Register server is: http://localhost:10100/eureka/\"', '', '2019-12-09 09:35:54');
INSERT INTO `sys_logs` VALUES (12, '67873b1b-5d98-4cd2-8d37-de3b5d4e2c3b', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '\"system-code is running on port: 10113. Register server is: http://localhost:10100/eureka/\"', '', '2019-12-09 09:38:17');
INSERT INTO `sys_logs` VALUES (13, 'ca39c7b7-05ca-4cb6-aac3-dadb3bc9841b', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '\"system-code is running on port: 10113. Register server is: http://localhost:10100/eureka/\"', '', '2019-12-09 09:41:40');
INSERT INTO `sys_logs` VALUES (14, 'fc5cd682-30d0-443c-841f-9c99402e50c5', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '\"system-code is running on port: 10113. Register server is: http://localhost:10100/eureka/\"', '', '2019-12-09 09:49:37');
INSERT INTO `sys_logs` VALUES (15, '71f4773c-9b0e-4011-ac33-0d006abe7281', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '\"system-code is running on port: 10113. Register server is: http://localhost:10100/eureka/\"', '', '2019-12-09 09:53:51');
INSERT INTO `sys_logs` VALUES (16, '34ec6229-dfc1-4c00-9887-cf28acd8345e', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '\"system-code is running on port: 10113. Register server is: http://localhost:10100/eureka/\"', '', '2019-12-20 12:54:20');
INSERT INTO `sys_logs` VALUES (17, '691140c6-cb6f-48d3-88e7-b6fe108e8b12', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '\"system-code is running on port: 10113. Register server is: http://localhost:10100/eureka/\"', '', '2019-12-20 12:54:27');
INSERT INTO `sys_logs` VALUES (18, '145843b3-88e9-4062-ba6b-eac10c174fe1', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '\"system-code is running on port: 10113. Register server is: http://localhost:10100/eureka/\"', '', '2019-12-20 13:00:01');
INSERT INTO `sys_logs` VALUES (19, '4c8f42f7-f93c-44d8-80bc-890995105ad9', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '\"system-code is running on port: 10113. Register server is: http://localhost:10100/eureka/\"', '', '2019-12-20 13:00:11');
INSERT INTO `sys_logs` VALUES (20, 'c3ed4b05-d352-4b42-b6bd-7b3d6c4138e1', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '\"system-code is running on port: 10113. Register server is: http://localhost:10100/eureka/\"', '', '2019-12-20 13:00:58');
INSERT INTO `sys_logs` VALUES (21, 'b4d69d9e-002d-49ad-85cc-4e1d7abb137f', 2, '12333AAA', 'eeeeeeeeeeeeee4324gf', 'tttt', ' rv = systemLogService.addLog(caller, account_uuid, type, title, contents, extra_info);', '', '2019-12-20 13:05:10');
INSERT INTO `sys_logs` VALUES (22, '1dbbcb4a-c1c6-4622-a6f2-4ef3af787106', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '\"system-code is running on port: 10113. Register server is: http://localhost:10100/eureka/\"', '', '2019-12-20 13:05:30');
INSERT INTO `sys_logs` VALUES (23, 'ea00117b-ab5d-4a0a-a157-5ac6cb90d78e', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '\"system-code is running on port: 10113. Register server is: http://localhost:10100/eureka/\"', '', '2019-12-20 13:05:52');
INSERT INTO `sys_logs` VALUES (24, 'fcf41711-99af-4e90-94c0-c0af9b9bf6b2', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '\"system-code is running on port: 10113. Register server is: http://localhost:10100/eureka/\"', '', '2019-12-20 13:23:31');
INSERT INTO `sys_logs` VALUES (25, 'c9813280-de94-48ec-af80-907d99c2a66f', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '\"system-code is running on port: 10113. Register server is: http://localhost:10100/eureka/\"', '', '2019-12-20 13:23:57');
INSERT INTO `sys_logs` VALUES (26, '9b1a9584-7423-4377-a23a-70431a770f5b', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '\"system-code is running on port: 10113. Register server is: http://localhost:10100/eureka/\"', '', '2019-12-20 13:24:14');
INSERT INTO `sys_logs` VALUES (27, '15045433-70d9-4329-a1a1-8458a200c69c', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '\"system-code is running on port: 10113. Register server is: http://localhost:10100/eureka/\"', '', '2019-12-20 13:24:18');
INSERT INTO `sys_logs` VALUES (28, '205227ad-dc31-4a69-9038-fa9281416963', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '\"system-code is running on port: 10113. Register server is: http://localhost:10100/eureka/\"', '', '2019-12-20 13:24:22');
INSERT INTO `sys_logs` VALUES (29, 'cd152f1c-4bab-4405-b105-c91be2f97532', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '\"system-code is running on port: 10113. Register server is: http://localhost:10100/eureka/\"', '', '2019-12-20 14:18:20');
INSERT INTO `sys_logs` VALUES (30, '9c2bb9a4-e036-4264-9a7b-5e6a9f9ee58a', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '\"system-code is running on port: 10113. Register server is: http://localhost:10100/eureka/\"', '', '2019-12-20 14:18:37');
INSERT INTO `sys_logs` VALUES (31, '7777e819-9a23-4e99-b82e-554c45fb34ad', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '\"system-code is running on port: 10113. Register server is: http://localhost:10100/eureka/\"', '', '2019-12-20 16:32:06');
INSERT INTO `sys_logs` VALUES (32, '42e141cb-8383-4102-9e10-11f13192660f', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '\"system-code is running on port: 10113. Register server is: http://localhost:10100/eureka/\"', '', '2019-12-20 16:37:28');
INSERT INTO `sys_logs` VALUES (33, '689ed592-e2ea-4c93-83e5-92333ea320a4', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '\"system-code is running on port: 10113. Register server is: http://localhost:10100/eureka/\"', '', '2019-12-20 16:38:03');
INSERT INTO `sys_logs` VALUES (34, 'f042e88a-3907-4e10-9392-251ea66c1c2b', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '\"system-code is running on port: 10113. Register server is: http://localhost:10100/eureka/\"', '', '2019-12-20 16:52:33');
INSERT INTO `sys_logs` VALUES (35, 'eedf384d-3d24-4ed8-bd0c-52ee06ce3fa6', 1, 'fw analyze back-end server', 'b3fa2914-a4c4-4dc3-978f-fe7ca3125abb', 'echoErrorCode', '\"system-code is running on port: 10113. Register server is: http://localhost:10100/eureka/\"', '', '2019-12-20 16:52:53');

SET FOREIGN_KEY_CHECKS = 1;
