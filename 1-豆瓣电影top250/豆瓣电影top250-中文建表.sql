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
DROP TABLE IF EXISTS `豆瓣电影top250`;
CREATE TABLE `豆瓣电影top250` (
  `排名` int(3) NOT NULL,
  `电影名` varchar(20) DEFAULT NULL,
  `豆瓣电影介绍页网址` varchar(100) DEFAULT NULL,
  `平均评分` double DEFAULT NULL,
  `评分人数` int(11) DEFAULT NULL,
  `一句话简评` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
