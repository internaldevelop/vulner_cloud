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

 Date: 08/07/2020 14:27:05
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
  `authenticate_flag` int(0) NULL DEFAULT 0 COMMENT '标识默认值0 (1:验证通过; 2:验签错误; 3:解密错误; 4:授信过期)',
  `sym_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对称秘钥',
  `public_key` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '公钥',
  `dev_fingerprint` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '设备指纹',
  `plaintext` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '明文',
  `ciphertext` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '密文',
  `signature` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '签名',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 65 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资产认证表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for asset_authenticate_record
-- ----------------------------
DROP TABLE IF EXISTS `asset_authenticate_record`;
CREATE TABLE `asset_authenticate_record`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'uuid',
  `asset_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资产uuid',
  `authenticate_flag` int(0) NULL DEFAULT 0 COMMENT '标识默认值0 (1:验证通过; 2:验签错误; 3:解密错误; 4:授信过期)',
  `sym_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对称秘钥',
  `public_key` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '公钥',
  `dev_fingerprint` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '设备指纹',
  `plaintext` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '明文',
  `ciphertext` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '密文',
  `signature` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '签名',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 553 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资产认证表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for asset_data_packet
-- ----------------------------
DROP TABLE IF EXISTS `asset_data_packet`;
CREATE TABLE `asset_data_packet`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'UUID',
  `asset_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资产uuid',
  `transport_protocol` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '传输协议tcp,udp,ICMP,arp',
  `app_protocol` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用协议',
  `direction` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '方向 1:上行; 2:下行',
  `source_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '源IP',
  `source_port` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '源端口',
  `dest_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目的IP',
  `dest_port` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目的端口',
  `parse_time` datetime(0) NULL DEFAULT NULL COMMENT '协议请求时间',
  `src_data` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '原始数据',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_uuid`(`uuid`) USING BTREE,
  INDEX `index_uuid_time`(`asset_uuid`, `create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 43850 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据包' ROW_FORMAT = Dynamic;

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
  `ipv4` varchar(300) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ipv6` varchar(300) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `bytes_recv` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '接收字节数',
  `bytes_sent` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '发送字节数',
  `packets_recv` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '接收数据包',
  `packets_sent` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '发送数据包',
  `asset_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资产uuid',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_uuid`(`uuid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1107022 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '流量监控数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for asset_perf_data
-- ----------------------------
DROP TABLE IF EXISTS `asset_perf_data`;
CREATE TABLE `asset_perf_data`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '资产性能核查信息集序号',
  `uuid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '本条扫描信息的uuid',
  `asset_uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '被核查的资产uuid',
  `cpu_free_percent` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'cpu空闲率',
  `cpu_used_percent` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'cpu使用率',
  `memory_free_percent` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内存空闲率',
  `memory_used_percent` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内存使用率',
  `disk_free_percent` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '磁盘空闲率',
  `disk_used_percent` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '磁盘使用率',
  `packets_recv` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `packets_sent` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `bytes_recv` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `bytes_sent` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '资产性能核查时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `asset_uuid`(`asset_uuid`) USING BTREE,
  INDEX `index_asset_uuid`(`asset_uuid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 233302 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for assets
-- ----------------------------
DROP TABLE IF EXISTS `assets`;
CREATE TABLE `assets`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资产的UUID',
  `classify` int(0) NULL DEFAULT 0 COMMENT '审核分类0: (1:通过(白名单); 2:拒绝(黑名单))',
  `code` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资产代号（可选）',
  `name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资产名称',
  `ip` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资产的IP地址',
  `port` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目标机器的端口',
  `os_type` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作系统的类型或系列(1:windows; 2:linux)',
  `os_ver` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作系统的版本',
  `on_line` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '在线状态 0:不在线; 1:在线',
  `expire_time` datetime(0) NULL DEFAULT NULL COMMENT '到期时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_uuid`(`uuid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 47 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '设备资产表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
