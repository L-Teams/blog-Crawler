/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50556
Source Host           : localhost:3306
Source Database       : itsearch

Target Server Type    : MYSQL
Target Server Version : 50556
File Encoding         : 65001

Date: 2017-06-17 17:04:33
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for csdnblog
-- ----------------------------
DROP TABLE IF EXISTS `csdnblog`;
CREATE TABLE `csdnblog` (
  `id` varchar(255) NOT NULL COMMENT '博客id',
  `url` varchar(255) NOT NULL COMMENT '博客的url',
  `author` varchar(255) DEFAULT NULL COMMENT '博客作者',
  `title` varchar(255) DEFAULT NULL COMMENT '博客标题',
  `date` date DEFAULT NULL COMMENT '博客创建日期',
  `tags` varchar(500) DEFAULT NULL COMMENT '博客标签',
  `category` varchar(500) DEFAULT NULL COMMENT '博客分类',
  `view` int(11) DEFAULT NULL COMMENT '博客观看人数',
  `comments` int(11) DEFAULT NULL COMMENT '博客评论人数',
  `copyright` tinyint(4) DEFAULT NULL COMMENT '是否是转载还是原创',
  `summery` text COMMENT '博客摘要',
  `digg` int(11) DEFAULT NULL COMMENT '博客顶的人数',
  `bury` tinyint(4) DEFAULT NULL COMMENT '博客踩的人数',
  `content` mediumtext COMMENT '博客内容',
  `createtime` date DEFAULT NULL COMMENT '记录添加时间',
  `updatetime` date DEFAULT NULL COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `url` (`url`) USING HASH COMMENT 'url列添加索引',
  UNIQUE KEY `id` (`id`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
