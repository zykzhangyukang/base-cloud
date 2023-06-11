/*
 Navicat Premium Data Transfer

 Source Server         : 本机mysql
 Source Server Type    : MySQL
 Source Server Version : 50728
 Source Host           : localhost:3306
 Source Schema         : base_auth

 Target Server Type    : MySQL
 Target Server Version : 50728
 File Encoding         : 65001

 Date: 11/06/2023 13:45:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for auth_dept
-- ----------------------------
DROP TABLE IF EXISTS `auth_dept`;
CREATE TABLE `auth_dept`  (
  `dept_id` int(11) NOT NULL COMMENT '部门id',
  `dept_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门编号',
  `dept_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of auth_dept
-- ----------------------------
INSERT INTO `auth_dept` VALUES (1, 'yfb', '研发部', '2022-03-12 11:51:12', '2022-03-12 11:51:13');
INSERT INTO `auth_dept` VALUES (2, 'csb', '测试部', '2022-03-12 11:51:49', '2022-03-12 11:51:51');
INSERT INTO `auth_dept` VALUES (3, 'khb1', '客户一部', '2022-03-12 16:29:08', '2022-03-12 16:29:10');
INSERT INTO `auth_dept` VALUES (4, 'khb2', '客户二部', '2022-03-12 16:29:27', '2022-03-12 16:29:29');
INSERT INTO `auth_dept` VALUES (5, 'yyb', '运营部', '2022-03-12 16:29:44', '2022-03-12 16:29:45');
INSERT INTO `auth_dept` VALUES (6, 'jyb', '交易部', '2022-03-12 16:30:04', '2022-03-12 16:30:06');
INSERT INTO `auth_dept` VALUES (7, 'yuwb', '运维部', '2022-03-20 15:42:39', '2022-03-20 15:42:40');

-- ----------------------------
-- Table structure for auth_func
-- ----------------------------
DROP TABLE IF EXISTS `auth_func`;
CREATE TABLE `auth_func`  (
  `func_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `func_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '功能名称',
  `func_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '功能key',
  `func_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '功能类型(目录/功能)',
  `func_icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目录图标',
  `func_sort` int(11) NULL DEFAULT NULL COMMENT '功能排序',
  `dir_hide` bit(1) NULL DEFAULT NULL COMMENT '是否隐藏',
  `parent_id` int(11) NOT NULL COMMENT '父级功能id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`func_id`) USING BTREE,
  UNIQUE INDEX `idx_func_key`(`func_key`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 99 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of auth_func
-- ----------------------------
INSERT INTO `auth_func` VALUES (1, '粒创商城', 'root', 'dir', 'HomeOutlined', 1, b'0', 0, '2022-03-19 15:56:27', '2022-03-19 15:56:29');
INSERT INTO `auth_func` VALUES (2, '权限系统', 'Auth', 'dir', 'ControlOutlined', 1, b'0', 1, '2022-03-19 15:57:28', '2022-03-19 15:57:31');
INSERT INTO `auth_func` VALUES (3, '用户管理', 'AuthUser', 'dir', 'UserAddOutlined', 3, b'0', 2, '2022-03-19 16:10:26', '2022-03-19 16:10:28');
INSERT INTO `auth_func` VALUES (4, '角色管理', 'AuthRole', 'dir', 'TrademarkCircleOutlined', 4, b'0', 2, '2022-03-19 16:11:29', '2022-03-19 16:11:30');
INSERT INTO `auth_func` VALUES (5, '功能管理', 'AuthFunc', 'dir', 'StarOutlined', 5, b'0', 2, '2022-03-19 16:11:41', '2022-03-19 16:11:42');
INSERT INTO `auth_func` VALUES (6, '资源管理', 'AuthResc', 'dir', 'UnlockOutlined', 6, b'0', 2, '2022-03-19 16:11:54', '2022-03-19 16:11:55');
INSERT INTO `auth_func` VALUES (8, '角色删除', 'auth:role:delete', 'func', 'HomeOutlined', 7, NULL, 4, '2022-03-19 18:23:22', '2022-03-19 18:23:25');
INSERT INTO `auth_func` VALUES (9, '角色更新', 'auth:role:update', 'func', 'HomeOutlined', 8, NULL, 4, '2022-03-19 18:23:38', '2022-03-19 18:23:40');
INSERT INTO `auth_func` VALUES (11, '角色查询', 'auth:role:page', 'func', 'HomeOutlined', 9, NULL, 4, '2022-03-19 18:24:02', '2022-03-19 18:24:03');
INSERT INTO `auth_func` VALUES (12, '功能查询', 'auth:func:page', 'func', 'HomeOutlined', 10, NULL, 5, '2022-03-19 18:24:30', '2022-03-19 18:24:32');
INSERT INTO `auth_func` VALUES (13, '资源查询', 'auth:resc:page', 'func', 'HomeOutlined', 11, NULL, 6, '2022-03-19 18:24:51', '2022-03-19 18:24:52');
INSERT INTO `auth_func` VALUES (14, '功能新增', 'auth:func:add', 'func', 'HomeOutlined', 12, NULL, 5, '2022-03-19 18:26:06', '2022-03-19 18:26:08');
INSERT INTO `auth_func` VALUES (26, '资源新增', 'auth:resc:add', 'func', 'HomeOutlined', 13, NULL, 6, '2022-03-20 21:43:55', '2022-03-20 21:43:55');
INSERT INTO `auth_func` VALUES (27, '资源删除', 'auth:resc:delete', 'func', 'HomeOutlined', 14, NULL, 6, '2022-03-20 21:44:03', '2022-03-20 21:44:03');
INSERT INTO `auth_func` VALUES (54, '功能删除', 'auth:func:delete', 'func', 'HomeOutlined', 15, NULL, 5, '2022-03-26 20:57:44', '2022-03-26 20:57:44');
INSERT INTO `auth_func` VALUES (55, '功能更新', 'auth:func:update', 'func', 'HomeOutlined', 16, NULL, 5, '2022-03-26 20:58:00', '2022-03-26 20:58:00');
INSERT INTO `auth_func` VALUES (56, '资源更新', 'auth:resc:update', 'func', 'HomeOutlined', 17, NULL, 6, '2022-03-26 21:00:16', '2022-03-26 21:00:16');
INSERT INTO `auth_func` VALUES (57, '用户查询', 'auth:user:page', 'func', 'HomeOutlined', 18, NULL, 3, '2022-03-26 21:04:37', '2022-03-26 21:04:37');
INSERT INTO `auth_func` VALUES (58, '用户启用', 'auth:user:enable', 'func', 'HomeOutlined', 19, NULL, 3, '2022-03-26 21:05:02', '2022-03-26 21:05:02');
INSERT INTO `auth_func` VALUES (59, '用户禁用', 'auth:user:disable', 'func', 'HomeOutlined', 20, NULL, 3, '2022-03-26 21:05:19', '2022-03-26 21:05:19');
INSERT INTO `auth_func` VALUES (60, '用户更新', 'auth:user:update', 'func', 'HomeOutlined', 21, NULL, 3, '2022-03-26 21:05:33', '2022-03-26 21:05:33');
INSERT INTO `auth_func` VALUES (61, '用户删除', 'auth:user:delete', 'func', 'HomeOutlined', 22, NULL, 3, '2022-03-26 21:05:55', '2022-03-26 21:05:55');
INSERT INTO `auth_func` VALUES (62, '分配角色', 'auth:user:assign', 'func', 'HomeOutlined', 23, NULL, 3, '2022-03-26 21:06:48', '2022-03-26 21:06:48');
INSERT INTO `auth_func` VALUES (63, '分配用户', 'auth:role:assign', 'func', 'HomeOutlined', 24, NULL, 4, '2022-03-26 21:08:39', '2022-03-26 21:08:39');
INSERT INTO `auth_func` VALUES (71, '授权功能', 'auth:role:auth', 'func', 'HomeOutlined', 25, NULL, 4, '2022-04-04 17:45:36', '2022-04-04 17:45:36');
INSERT INTO `auth_func` VALUES (72, '角色新增', 'auth:role:add', 'func', 'HomeOutlined', 26, NULL, 4, '2022-04-16 11:13:19', '2022-04-16 11:13:19');
INSERT INTO `auth_func` VALUES (73, '解绑用户', 'auth:func:unbind', 'func', 'HomeOutlined', 27, NULL, 5, '2022-04-16 21:55:19', '2022-04-16 21:55:19');
INSERT INTO `auth_func` VALUES (74, '解绑资源', 'auth:resc:unbind', 'func', 'HomeOutlined', 28, NULL, 5, '2022-04-16 22:02:49', '2022-04-16 22:02:49');
INSERT INTO `auth_func` VALUES (75, '用户新增', 'auth:user:add', 'func', 'HomeOutlined', 29, NULL, 3, '2022-05-02 11:40:57', '2022-05-02 11:40:57');
INSERT INTO `auth_func` VALUES (84, '修改密码', 'auth:user:updatepwd', 'func', 'HomeOutlined', 30, NULL, 3, '2022-05-14 12:20:12', '2022-05-14 12:20:12');
INSERT INTO `auth_func` VALUES (93, '同步系统', 'Sync', 'dir', 'CloudOutlined', 1, b'0', 1, '2022-09-13 20:54:26', '2022-09-13 20:54:26');
INSERT INTO `auth_func` VALUES (94, '同步计划', 'SyncPlan', 'dir', 'ReconciliationOutlined', 1, b'0', 93, '2022-09-13 20:54:58', '2022-09-13 20:54:58');
INSERT INTO `auth_func` VALUES (95, '同步记录', 'SyncResult', 'dir', 'PoundOutlined', 1, b'0', 93, '2022-12-04 23:48:14', '2022-12-04 23:48:14');
INSERT INTO `auth_func` VALUES (96, '消息回调', 'SyncCallback', 'dir', 'ReloadOutlined', 3, b'0', 93, '2022-12-04 23:48:39', '2022-12-04 23:48:39');
INSERT INTO `auth_func` VALUES (97, '同步消息', 'SyncMsg', 'dir', 'RobotOutlined', 5, b'0', 93, '2022-12-04 23:49:04', '2022-12-04 23:49:04');
INSERT INTO `auth_func` VALUES (98, '控制面板', 'Index', 'dir', 'HomeOutlined', -1, b'0', 1, '2023-06-11 13:33:57', '2023-06-11 13:33:58');

-- ----------------------------
-- Table structure for auth_func_resc
-- ----------------------------
DROP TABLE IF EXISTS `auth_func_resc`;
CREATE TABLE `auth_func_resc`  (
  `func_resc_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `func_id` int(11) NOT NULL COMMENT '功能id',
  `resc_id` int(11) NOT NULL COMMENT '资源id',
  PRIMARY KEY (`func_resc_id`) USING BTREE,
  INDEX `idx_func_id`(`func_id`) USING BTREE,
  INDEX `idx_resc_id`(`resc_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 337 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of auth_func_resc
-- ----------------------------
INSERT INTO `auth_func_resc` VALUES (286, 75, 27);
INSERT INTO `auth_func_resc` VALUES (287, 62, 9);
INSERT INTO `auth_func_resc` VALUES (288, 62, 10);
INSERT INTO `auth_func_resc` VALUES (289, 61, 3);
INSERT INTO `auth_func_resc` VALUES (290, 60, 4);
INSERT INTO `auth_func_resc` VALUES (291, 60, 2);
INSERT INTO `auth_func_resc` VALUES (292, 60, 11);
INSERT INTO `auth_func_resc` VALUES (293, 59, 14);
INSERT INTO `auth_func_resc` VALUES (297, 58, 12);
INSERT INTO `auth_func_resc` VALUES (301, 56, 23);
INSERT INTO `auth_func_resc` VALUES (302, 56, 22);
INSERT INTO `auth_func_resc` VALUES (303, 56, 41);
INSERT INTO `auth_func_resc` VALUES (304, 55, 30);
INSERT INTO `auth_func_resc` VALUES (305, 55, 29);
INSERT INTO `auth_func_resc` VALUES (306, 54, 31);
INSERT INTO `auth_func_resc` VALUES (307, 27, 24);
INSERT INTO `auth_func_resc` VALUES (308, 26, 21);
INSERT INTO `auth_func_resc` VALUES (309, 14, 32);
INSERT INTO `auth_func_resc` VALUES (312, 73, 39);
INSERT INTO `auth_func_resc` VALUES (313, 72, 19);
INSERT INTO `auth_func_resc` VALUES (316, 63, 35);
INSERT INTO `auth_func_resc` VALUES (317, 63, 36);
INSERT INTO `auth_func_resc` VALUES (318, 63, 13);
INSERT INTO `auth_func_resc` VALUES (319, 8, 15);
INSERT INTO `auth_func_resc` VALUES (320, 9, 16);
INSERT INTO `auth_func_resc` VALUES (321, 9, 17);
INSERT INTO `auth_func_resc` VALUES (322, 11, 18);
INSERT INTO `auth_func_resc` VALUES (323, 12, 33);
INSERT INTO `auth_func_resc` VALUES (324, 12, 34);
INSERT INTO `auth_func_resc` VALUES (327, 57, 1);
INSERT INTO `auth_func_resc` VALUES (328, 57, 11);
INSERT INTO `auth_func_resc` VALUES (329, 13, 20);
INSERT INTO `auth_func_resc` VALUES (330, 13, 41);
INSERT INTO `auth_func_resc` VALUES (331, 71, 38);
INSERT INTO `auth_func_resc` VALUES (332, 71, 37);
INSERT INTO `auth_func_resc` VALUES (333, 71, 43);
INSERT INTO `auth_func_resc` VALUES (335, 84, 42);
INSERT INTO `auth_func_resc` VALUES (336, 74, 40);

-- ----------------------------
-- Table structure for auth_resc
-- ----------------------------
DROP TABLE IF EXISTS `auth_resc`;
CREATE TABLE `auth_resc`  (
  `resc_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '资源id',
  `resc_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源名称',
  `resc_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源url',
  `resc_domain` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源所属系统',
  `method_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求方式',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`resc_id`) USING BTREE,
  UNIQUE INDEX `idx_resc_url`(`resc_url`) USING BTREE,
  INDEX `idx_resc_name`(`resc_name`) USING BTREE,
  INDEX `idx_resc_domain`(`resc_domain`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 44 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of auth_resc
-- ----------------------------
INSERT INTO `auth_resc` VALUES (1, '用户列表', '/auth/user/page', 'auth', 'get', '2022-03-19 09:31:31', '2022-04-09 14:03:50');
INSERT INTO `auth_resc` VALUES (2, '用户获取', '/auth/user/select', 'auth', 'get', '2022-03-19 09:34:24', '2022-05-10 22:41:35');
INSERT INTO `auth_resc` VALUES (3, '用户删除', '/auth/user/delete', 'auth', 'get', '2022-03-19 09:34:40', '2022-05-01 23:18:10');
INSERT INTO `auth_resc` VALUES (4, '用户更新', '/auth/user/update', 'auth', 'post', '2022-03-19 09:34:58', '2022-05-10 22:36:29');
INSERT INTO `auth_resc` VALUES (9, '用户分配角色初始化', '/auth/user/role/update/init', 'auth', 'get', '2022-03-19 10:29:15', '2022-08-30 23:07:46');
INSERT INTO `auth_resc` VALUES (10, '用户分配角色', '/auth/user/role/update', 'auth', 'post', '2022-03-19 10:29:41', '2022-09-04 14:33:38');
INSERT INTO `auth_resc` VALUES (11, '部门列表', '/auth/dept/list', 'auth', 'get', '2022-03-19 10:30:01', '2022-04-09 14:03:20');
INSERT INTO `auth_resc` VALUES (12, '用户启用', '/auth/user/update/enable', 'auth', 'get', '2022-03-19 10:30:16', '2022-09-03 15:45:31');
INSERT INTO `auth_resc` VALUES (13, '用户角色信息', '/auth/user/select/role/names', 'auth', 'get', '2022-03-19 10:30:34', '2022-04-09 14:03:07');
INSERT INTO `auth_resc` VALUES (14, '用户禁用', '/auth/user/update/disable', 'auth', 'get', '2022-03-19 10:30:48', '2022-05-14 11:52:30');
INSERT INTO `auth_resc` VALUES (15, '角色删除', '/auth/role/delete', 'auth', 'get', '2022-03-19 10:31:30', '2022-05-01 23:18:07');
INSERT INTO `auth_resc` VALUES (16, '角色更新', '/auth/role/update', 'auth', 'post', '2022-03-19 10:31:44', '2022-05-01 22:23:59');
INSERT INTO `auth_resc` VALUES (17, '角色获取', '/auth/role/select', 'auth', 'get', '2022-03-19 10:32:06', '2022-04-09 14:02:34');
INSERT INTO `auth_resc` VALUES (18, '角色列表', '/auth/role/page', 'auth', 'get', '2022-03-19 10:32:28', '2022-04-09 14:02:25');
INSERT INTO `auth_resc` VALUES (19, '角色新增', '/auth/role/save', 'auth', 'post', '2022-03-19 10:32:48', '2022-04-09 14:02:29');
INSERT INTO `auth_resc` VALUES (20, '资源列表', '/auth/resc/page', 'auth', 'get', '2022-03-19 10:33:08', '2022-09-04 14:44:14');
INSERT INTO `auth_resc` VALUES (21, '资源新增', '/auth/resc/save', 'auth', 'post', '2022-03-19 10:33:26', '2022-04-09 14:02:15');
INSERT INTO `auth_resc` VALUES (22, '资源获取', '/auth/resc/select', 'auth', 'get', '2022-03-19 10:33:42', '2022-04-09 14:02:06');
INSERT INTO `auth_resc` VALUES (23, '资源更新', '/auth/resc/update', 'auth', 'post', '2022-03-19 10:33:56', '2022-05-01 22:23:25');
INSERT INTO `auth_resc` VALUES (24, '资源删除', '/auth/resc/delete', 'auth', 'get', '2022-03-19 10:34:13', '2022-05-01 23:17:58');
INSERT INTO `auth_resc` VALUES (27, '用户新增', '/auth/user/save', 'auth', 'post', '2022-03-19 11:18:04', '2022-04-09 14:01:38');
INSERT INTO `auth_resc` VALUES (29, '功能更新', '/auth/func/update', 'auth', 'post', '2022-03-26 20:52:38', '2022-05-01 21:57:53');
INSERT INTO `auth_resc` VALUES (30, '功能获取', '/auth/func/select', 'auth', 'get', '2022-03-26 20:52:51', '2022-04-09 14:01:25');
INSERT INTO `auth_resc` VALUES (31, '功能删除', '/auth/func/delete', 'auth', 'get', '2022-03-26 20:53:09', '2022-05-01 23:17:54');
INSERT INTO `auth_resc` VALUES (32, '功能新增', '/auth/func/save', 'auth', 'post', '2022-03-26 20:53:24', '2022-04-09 14:01:12');
INSERT INTO `auth_resc` VALUES (33, '功能查询', '/auth/func/page', 'auth', 'get', '2022-03-26 20:53:40', '2022-09-11 16:05:51');
INSERT INTO `auth_resc` VALUES (34, '功能树获取', '/auth/func/list/tree', 'auth', 'get', '2022-03-26 20:53:58', '2022-04-09 14:01:03');
INSERT INTO `auth_resc` VALUES (35, '角色分配用户初始化', '/auth/role/user/update/init', 'auth', 'get', '2022-03-26 21:07:44', '2022-09-03 16:02:34');
INSERT INTO `auth_resc` VALUES (36, '角色分配用户', '/auth/role/user/update', 'auth', 'post', '2022-03-26 21:07:59', '2022-09-03 16:02:22');
INSERT INTO `auth_resc` VALUES (37, '角色授权功能初始化', '/auth/role/func/update/init', 'auth', 'get', '2022-04-04 17:46:28', '2022-09-03 16:02:00');
INSERT INTO `auth_resc` VALUES (38, '角色分配功能', '/auth/role/func/update', 'auth', 'post', '2022-04-04 17:46:57', '2022-08-30 23:00:27');
INSERT INTO `auth_resc` VALUES (39, '功能解绑用户', '/auth/func/delete/user/bind', 'auth', 'get', '2022-04-16 21:54:38', '2022-04-16 22:02:08');
INSERT INTO `auth_resc` VALUES (40, '功能解绑资源', '/auth/func/delete/resc/bind', 'auth', 'get', '2022-04-16 22:02:24', '2022-04-23 21:28:09');
INSERT INTO `auth_resc` VALUES (41, '资源搜索', '/auth/resc/search', 'auth', 'get', '2022-05-01 19:17:13', '2022-09-01 00:02:42');
INSERT INTO `auth_resc` VALUES (42, '用户修改密码', '/auth/user/update/password', 'auth', 'post', '2022-05-14 12:19:03', '2022-09-03 11:24:39');
INSERT INTO `auth_resc` VALUES (43, '角色授权功能预检查', '/auth/role/func/update/check', 'auth', 'post', '2022-05-21 15:34:56', '2022-10-07 21:40:45');

-- ----------------------------
-- Table structure for auth_role
-- ----------------------------
DROP TABLE IF EXISTS `auth_role`;
CREATE TABLE `auth_role`  (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE INDEX `idx_role_name`(`role_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of auth_role
-- ----------------------------
INSERT INTO `auth_role` VALUES (42, '管理员', '高权限', '2022-04-16 11:38:56', '2022-09-11 16:24:52');
INSERT INTO `auth_role` VALUES (43, '普通用户', '低权限', '2022-04-16 11:39:07', '2022-09-11 16:32:38');
INSERT INTO `auth_role` VALUES (44, '查询角色', '只有查询的功能', '2022-09-12 10:56:43', '2022-10-07 21:40:37');
INSERT INTO `auth_role` VALUES (45, '查询和新增', '有查询和新增的功能权限', '2022-09-12 10:59:11', '2022-10-07 21:40:34');

-- ----------------------------
-- Table structure for auth_role_func
-- ----------------------------
DROP TABLE IF EXISTS `auth_role_func`;
CREATE TABLE `auth_role_func`  (
  `role_func_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `func_id` int(11) NOT NULL COMMENT '功能id',
  PRIMARY KEY (`role_func_id`) USING BTREE,
  INDEX `idx_role_id`(`role_id`) USING BTREE,
  INDEX `idx_func_id`(`func_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6381 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of auth_role_func
-- ----------------------------
INSERT INTO `auth_role_func` VALUES (6127, 43, 1);
INSERT INTO `auth_role_func` VALUES (6145, 44, 1);
INSERT INTO `auth_role_func` VALUES (6341, 45, 2);
INSERT INTO `auth_role_func` VALUES (6342, 45, 1);
INSERT INTO `auth_role_func` VALUES (6343, 45, 3);
INSERT INTO `auth_role_func` VALUES (6344, 45, 57);
INSERT INTO `auth_role_func` VALUES (6345, 42, 2);
INSERT INTO `auth_role_func` VALUES (6346, 42, 5);
INSERT INTO `auth_role_func` VALUES (6347, 42, 54);
INSERT INTO `auth_role_func` VALUES (6348, 42, 12);
INSERT INTO `auth_role_func` VALUES (6349, 42, 74);
INSERT INTO `auth_role_func` VALUES (6350, 42, 14);
INSERT INTO `auth_role_func` VALUES (6351, 42, 55);
INSERT INTO `auth_role_func` VALUES (6352, 42, 73);
INSERT INTO `auth_role_func` VALUES (6353, 42, 6);
INSERT INTO `auth_role_func` VALUES (6354, 42, 27);
INSERT INTO `auth_role_func` VALUES (6355, 42, 13);
INSERT INTO `auth_role_func` VALUES (6356, 42, 26);
INSERT INTO `auth_role_func` VALUES (6357, 42, 56);
INSERT INTO `auth_role_func` VALUES (6358, 42, 4);
INSERT INTO `auth_role_func` VALUES (6359, 42, 63);
INSERT INTO `auth_role_func` VALUES (6360, 42, 71);
INSERT INTO `auth_role_func` VALUES (6361, 42, 8);
INSERT INTO `auth_role_func` VALUES (6362, 42, 11);
INSERT INTO `auth_role_func` VALUES (6363, 42, 72);
INSERT INTO `auth_role_func` VALUES (6364, 42, 9);
INSERT INTO `auth_role_func` VALUES (6365, 42, 1);
INSERT INTO `auth_role_func` VALUES (6366, 42, 93);
INSERT INTO `auth_role_func` VALUES (6367, 42, 96);
INSERT INTO `auth_role_func` VALUES (6368, 42, 97);
INSERT INTO `auth_role_func` VALUES (6369, 42, 94);
INSERT INTO `auth_role_func` VALUES (6370, 42, 95);
INSERT INTO `auth_role_func` VALUES (6371, 42, 3);
INSERT INTO `auth_role_func` VALUES (6372, 42, 62);
INSERT INTO `auth_role_func` VALUES (6373, 42, 61);
INSERT INTO `auth_role_func` VALUES (6374, 42, 59);
INSERT INTO `auth_role_func` VALUES (6375, 42, 58);
INSERT INTO `auth_role_func` VALUES (6376, 42, 57);
INSERT INTO `auth_role_func` VALUES (6377, 42, 75);
INSERT INTO `auth_role_func` VALUES (6378, 42, 60);
INSERT INTO `auth_role_func` VALUES (6379, 42, 84);
INSERT INTO `auth_role_func` VALUES (6380, 42, 98);

-- ----------------------------
-- Table structure for auth_user
-- ----------------------------
DROP TABLE IF EXISTS `auth_user`;
CREATE TABLE `auth_user`  (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户账号',
  `real_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '真实名称',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `dept_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门编号',
  `user_status` int(255) NOT NULL COMMENT '状态',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `ix_username`(`username`) USING BTREE,
  INDEX `ix_deptcode`(`dept_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 69 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of auth_user
-- ----------------------------
INSERT INTO `auth_user` VALUES (61, 'admin', '小章鱼', 'e10adc3949ba59abbe56e057f20f883e', 'yfb', 1, '2022-05-01 23:27:25', '2022-05-21 16:57:00');
INSERT INTO `auth_user` VALUES (63, 'justIn', '贾斯汀', 'e10adc3949ba59abbe56e057f20f883e', 'csb', 0, '2022-05-04 19:41:43', '2022-05-11 21:35:03');
INSERT INTO `auth_user` VALUES (65, 'perter', '皮特', 'e10adc3949ba59abbe56e057f20f883e', 'khb1', 0, '2022-09-03 11:25:37', '2022-09-03 11:25:37');
INSERT INTO `auth_user` VALUES (66, 'whitelist', '怀特', 'e10adc3949ba59abbe56e057f20f883e', 'jyb', 0, '2022-09-03 11:26:13', '2022-09-03 15:46:14');
INSERT INTO `auth_user` VALUES (68, 'query', '查询', 'e10adc3949ba59abbe56e057f20f883e', 'yfb', 0, '2022-09-12 10:56:59', '2022-09-12 10:56:59');

-- ----------------------------
-- Table structure for auth_user_role
-- ----------------------------
DROP TABLE IF EXISTS `auth_user_role`;
CREATE TABLE `auth_user_role`  (
  `user_role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '组件',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`user_role_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_role_id`(`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 864 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of auth_user_role
-- ----------------------------
INSERT INTO `auth_user_role` VALUES (822, 61, 42);
INSERT INTO `auth_user_role` VALUES (833, 66, 42);
INSERT INTO `auth_user_role` VALUES (835, 65, 42);
INSERT INTO `auth_user_role` VALUES (856, 66, 43);
INSERT INTO `auth_user_role` VALUES (857, 65, 43);
INSERT INTO `auth_user_role` VALUES (862, 63, 43);
INSERT INTO `auth_user_role` VALUES (863, 68, 44);

SET FOREIGN_KEY_CHECKS = 1;
