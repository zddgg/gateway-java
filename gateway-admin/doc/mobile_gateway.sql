/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80027 (8.0.27)
 Source Host           : localhost:3306
 Source Schema         : mobile_gateway

 Target Server Type    : MySQL
 Target Server Version : 80027 (8.0.27)
 File Encoding         : 65001

 Date: 06/01/2023 21:33:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for application_info
-- ----------------------------
DROP TABLE IF EXISTS `application_info`;
CREATE TABLE `application_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `app_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '应用编号',
  `app_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '应用名称',
  `app_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '应用key',
  `app_secret` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `enterprise_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '企业编号',
  `create_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人员编号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新人员编号',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `application_info_app_id`(`app_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '应用信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of application_info
-- ----------------------------
INSERT INTO `application_info` VALUES (1, '100', '测试应用一', '100', '100', '4bc8920d3c754ab3a05f241fd6fbc62e', 'x\'x', '2022-12-15 19:10:22', 'xx', '2022-12-25 14:39:37');

-- ----------------------------
-- Table structure for application_route
-- ----------------------------
DROP TABLE IF EXISTS `application_route`;
CREATE TABLE `application_route`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `app_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '应用编号',
  `route_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '路由编号',
  `create_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人员编号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新人员编号',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '应用路由表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of application_route
-- ----------------------------
INSERT INTO `application_route` VALUES (1, '100', '100', 'xx', '2022-12-15 20:04:40', 'xx', '2022-12-15 20:04:43');

-- ----------------------------
-- Table structure for enterprise_info
-- ----------------------------
DROP TABLE IF EXISTS `enterprise_info`;
CREATE TABLE `enterprise_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `enterprise_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '企业编号',
  `enterprise_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '企业名称',
  `create_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人员编号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新人员编号',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `enterprise_info_enterprise_id`(`enterprise_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '企业信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of enterprise_info
-- ----------------------------
INSERT INTO `enterprise_info` VALUES (2, '4bc8920d3c754ab3a05f241fd6fbc62e', 'cc1', 'xx', '2022-12-25 02:13:29', 'xx', '2022-12-25 02:13:34');

-- ----------------------------
-- Table structure for node_info
-- ----------------------------
DROP TABLE IF EXISTS `node_info`;
CREATE TABLE `node_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `host` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'host',
  `port` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'port',
  `create_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人员编号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新人员编号',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `application_info_app_id`(`host` ASC, `port` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '节点信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of node_info
-- ----------------------------
INSERT INTO `node_info` VALUES (1, '172.18.176.1', '12800', 'xx', '2022-12-26 00:15:45', 'xx', '2022-12-26 00:15:45');
INSERT INTO `node_info` VALUES (2, '172.18.176.1', '12900', 'xx', '2022-12-26 00:20:26', 'xx', '2022-12-26 00:20:26');
INSERT INTO `node_info` VALUES (3, '172.18.176.1', '12901', 'xx', '2022-12-26 00:21:06', 'xx', '2022-12-26 00:21:06');
INSERT INTO `node_info` VALUES (4, '192.168.254.1', '12800', 'xx', '2022-12-31 23:09:32', 'xx', '2022-12-31 23:09:32');

-- ----------------------------
-- Table structure for route_info
-- ----------------------------
DROP TABLE IF EXISTS `route_info`;
CREATE TABLE `route_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `route_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '路由编号',
  `route_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '路由名称',
  `route_version` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '路由版本',
  `route_path` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `upstream_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '上游编号',
  `create_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人员编号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新人员编号',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `route_info_route_id`(`route_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'API信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of route_info
-- ----------------------------
INSERT INTO `route_info` VALUES (1, '100', '测试路由一', '1', '/test', '100', 'xx', '2022-12-15 20:02:55', 'xx', '2022-12-15 20:02:57');
INSERT INTO `route_info` VALUES (7, 'e1fcc7320f2a427eb55abc205501e31d', '11', '1', '/sss', '100', 'xx', '2022-12-24 22:48:59', 'xx', '2022-12-25 14:54:41');
INSERT INTO `route_info` VALUES (8, 'f8ba90ec117446a4b6702a043f36b352', '12', '1', '/as', '100', 'xx', '2022-12-24 22:49:19', 'xx', '2022-12-24 22:49:19');

-- ----------------------------
-- Table structure for secret_info
-- ----------------------------
DROP TABLE IF EXISTS `secret_info`;
CREATE TABLE `secret_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `secret_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密钥编号',
  `secret_value` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密钥值',
  `encoding_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '编码类型',
  `create_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人员编号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新人员编号',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `secret_info_secret_id`(`secret_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '密钥信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of secret_info
-- ----------------------------

-- ----------------------------
-- Table structure for upstream_info
-- ----------------------------
DROP TABLE IF EXISTS `upstream_info`;
CREATE TABLE `upstream_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `upstream_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '上游编号',
  `upstream_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '上游名称',
  `upstream_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '上游类型',
  `http_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '上游地址',
  `discovery_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '服务发现类型',
  `create_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人员编号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新人员编号',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upstream_info_upstream_id`(`upstream_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '上游信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of upstream_info
-- ----------------------------
INSERT INTO `upstream_info` VALUES (1, '100', '测试上游一', '0', 'http://localhost:12301/test', NULL, 'xx', '2022-12-15 20:04:00', 'xx', '2022-12-24 22:41:13');

SET FOREIGN_KEY_CHECKS = 1;
