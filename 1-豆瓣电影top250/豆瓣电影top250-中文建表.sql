/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50621
Source Host           : localhost:3306
Source Database       : jsoup-test

Target Server Type    : MYSQL
Target Server Version : 50621
File Encoding         : 65001

Date: 2019-08-12 20:08:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for movie
-- ----------------------------
DROP TABLE IF EXISTS `�����Ӱtop250`;
CREATE TABLE `�����Ӱtop250` (
  `����` int(3) NOT NULL,
  `��Ӱ��` varchar(20) DEFAULT NULL,
  `�����Ӱ����ҳ��ַ` varchar(100) DEFAULT NULL,
  `ƽ������` double DEFAULT NULL,
  `��������` int(11) DEFAULT NULL,
  `һ�仰����` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
