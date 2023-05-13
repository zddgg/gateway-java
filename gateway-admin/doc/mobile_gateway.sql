/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80032 (8.0.32)
 Source Host           : localhost:3306
 Source Schema         : mobile_gateway

 Target Server Type    : MySQL
 Target Server Version : 80032 (8.0.32)
 File Encoding         : 65001

 Date: 13/05/2023 20:44:14
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
  `cryptor_enable` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求是否加密',
  `secret_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密钥编号',
  `request_secret_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求密钥编号',
  `response_secret_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '响应密钥编号',
  `create_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人员编号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新人员编号',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `application_info_app_id`(`app_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '应用信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of application_info
-- ----------------------------
INSERT INTO `application_info` VALUES (4, '80b9e0fbd0c24d4c86fcc18c0f29f350', '测试应用一', '100', '100100', 'cfee2b12c37341a6aba7fb5888dfc3fe', '1', NULL, 'c3d8ea08491749d48ae1466e6827ed36', '4c331e07fdc34f74b94b3ad9b73ee105', 'xx', '2023-02-09 17:02:26', 'xx', '2023-02-11 00:21:06');

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
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '应用路由表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of application_route
-- ----------------------------

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
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '企业信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of enterprise_info
-- ----------------------------
INSERT INTO `enterprise_info` VALUES (3, 'cfee2b12c37341a6aba7fb5888dfc3fe', '测试企业一', 'xx', '2023-02-09 16:57:14', 'xx', '2023-02-09 16:57:14');

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
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '节点信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of node_info
-- ----------------------------
INSERT INTO `node_info` VALUES (7, '192.168.254.1', '12800', 'xx', '2023-02-09 17:06:17', 'xx', '2023-02-09 17:06:17');

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
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'API信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of route_info
-- ----------------------------
INSERT INTO `route_info` VALUES (9, 'd82798f65c6941b2ab71420c53ba8086', '测试路由一', '1.0', '/test', '1c2a34569bda41018faf2148a51909c4', 'xx', '2023-02-09 17:05:47', 'xx', '2023-02-09 17:05:47');

-- ----------------------------
-- Table structure for secret_info
-- ----------------------------
DROP TABLE IF EXISTS `secret_info`;
CREATE TABLE `secret_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `secret_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密钥编号',
  `secret_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密钥名称',
  `secret_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密钥类型',
  `secret_key` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密钥值',
  `public_key` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公钥',
  `private_key` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '私钥',
  `encoding_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '编码类型',
  `create_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人员编号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新人员编号',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `secret_info_secret_id`(`secret_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '密钥信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of secret_info
-- ----------------------------
INSERT INTO `secret_info` VALUES (5, 'c3d8ea08491749d48ae1466e6827ed36', '测试密钥一', '2', 'xxx', '042ab0373687b91f7590b099077427a2dc042ebd89b489c5371baa36e41e256e04b6be18714d71e42355554e4f20f1c4ead73579155c396077ca953c433eb9a5a2', '26882bcbb54dd291dc40393f65555cbd042b017ce1bba6b48d1c390c4ff57b18', '1', 'xx', '2023-02-09 16:59:17', 'xx', '2023-02-10 20:26:41');
INSERT INTO `secret_info` VALUES (6, '4c331e07fdc34f74b94b3ad9b73ee105', '网关测试密钥一', '2', NULL, '042c7e39a40bf7383b5d13f1e0dad1e6ac1ea7e26ab7700b265a5748a8eb0985ebdc27f04c30b9062d54178cf81e89b519cab70629eac275214149b78450f0b9f6', '46e42eae1eb1bf3e6c6ee158dd2e2f4a3a3837677358da2804e6921c7358954d', '1', 'xx', '2023-02-09 17:01:19', 'xx', '2023-02-10 20:26:53');

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
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '上游信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of upstream_info
-- ----------------------------
INSERT INTO `upstream_info` VALUES (9, '1c2a34569bda41018faf2148a51909c4', '测试上游一', '0', 'http://127.0.0.1:8081/demo/test1', NULL, 'xx', '2023-02-09 17:04:38', 'xx', '2023-02-09 17:04:38');

SET FOREIGN_KEY_CHECKS = 1;
