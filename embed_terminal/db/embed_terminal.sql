/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.1.100
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : 192.168.1.100:13306
 Source Schema         : embed_terminal

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 29/04/2020 09:20:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for asset_authenticate
-- ----------------------------
DROP TABLE IF EXISTS `asset_authenticate`;
CREATE TABLE `asset_authenticate`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'uuid',
  `asset_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资产uuid',
  `authenticate_flag` int(0) NULL DEFAULT 0 COMMENT '认证标识默认值0未认证(-1:失败；1:成功)',
  `sym_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对称秘钥',
  `public_key` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '公钥',
  `dev_fingerprint` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '设备指纹',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资产认证表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of asset_authenticate
-- ----------------------------
INSERT INTO `asset_authenticate` VALUES (27, 'ad4b4f65-c7ff-4e47-8830-fbff354f4126', '12aae60b-7812-4155-8d1e-bab6ac5f5349', 1, 'f52da1cfdf5c8b0c1376c7aaf1ebf4a94a9a0b94092c7d5fc844d0f7db879ef3', 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIQcyTPIBDPLrIumxbczruwfN+NYMCX9p7poBTyQLVUk\n5VplPwLw55v7U8Xn9oOsXjIvJZaVT+LuPO38p5awRL8CAwEAAQ==', '{\"CPU_processorID\":\"BFEBFBFF000506E3\",\"os\":\"Windows 10\",\"CPU_name\":\"Intel(R) Core(TM) i7-6700HQ CPU @ 2.60GHz\",\"CPU_logicalProcessorCount\":8,\"ComputerSystem_baseboard\":{\"manufacturer\":\"ASUSTeK COMPUTER INC.\",\"model\":\"\",\"serialNumber\":\"BSN12345678901234567\",\"version\":\"1.0       \"}}', '2020-04-21 10:57:16', '2020-04-21 10:55:36');
INSERT INTO `asset_authenticate` VALUES (28, 'd3852890-7849-4566-b8aa-a6ff54ed1023', 'fb9ac6e7-d12e-4808-87cd-02b67067e6ce', 0, '919600e83add11bf508a69d01b45c4aa18d5bd2cce95c741aeded7149437a33b', NULL, '{\"CPU_processorID\":\"EA 06 09 00 FF FB EB BF\",\"os\":\"Linux\",\"CPU_name\":\"Intel(R) Core(TM) i7-8700 CPU @ 3.20GHz\",\"CPU_logicalProcessorCount\":12,\"ComputerSystem_baseboard\":{\"manufacturer\":\"HP\",\"model\":\"83E0\",\"serialNumber\":\"PGTXH0JCYC98EB\",\"version\":\"KBC Version 07.D2.00\"}}', '2020-04-26 17:42:18', '2020-04-26 17:42:18');

-- ----------------------------
-- Table structure for asset_network
-- ----------------------------
DROP TABLE IF EXISTS `asset_network`;
CREATE TABLE `asset_network`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'UUID',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '网卡名称',
  `mac_address` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'MAC',
  `mtu` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '最大传输单元',
  `speed` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '速度',
  `ipv4` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ipv6` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `bytes_recv` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '接收字节数',
  `bytes_sent` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '发送字节数',
  `packets_recv` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '接收数据包',
  `packets_sent` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '发送数据包',
  `asset_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资产uuid',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '流量监控数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for assets
-- ----------------------------
DROP TABLE IF EXISTS `assets`;
CREATE TABLE `assets`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资产的UUID',
  `empower_flag` int(0) NULL DEFAULT 0 COMMENT '授权标识默认值0: (1:通过; -1:拒绝)',
  `code` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资产代号（可选）',
  `name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资产名称',
  `ip` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资产的IP地址',
  `port` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目标机器的端口',
  `os_type` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作系统的类型或系列(1:windows; 2:linux)',
  `os_ver` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作系统的版本',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_uuid`(`uuid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '设备资产表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of assets
-- ----------------------------
INSERT INTO `assets` VALUES (9, '12aae60b-7812-4155-8d1e-bab6ac5f5349', 1, NULL, 'baixd', '192.168.1.40', NULL, '1', '10.0', '2020-04-21 10:57:03', '2020-04-21 10:55:35');
INSERT INTO `assets` VALUES (10, 'fb9ac6e7-d12e-4808-87cd-02b67067e6ce', 0, NULL, 'root', '192.168.1.100', NULL, '-1', '3.10.0-693.el7.x86_64', NULL, '2020-04-26 17:42:18');

SET FOREIGN_KEY_CHECKS = 1;
