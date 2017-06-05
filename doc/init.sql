/*
SQLyog v10.2 
MySQL - 5.5.56 : Database - itsearch
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`itsearch` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `itsearch`;

/*Table structure for table `blog` */

DROP TABLE IF EXISTS `blog`;

CREATE TABLE `blog` (
  `id` varchar(200) NOT NULL COMMENT '编号',
  `url` varchar(500) NOT NULL COMMENT '博客url',
  `author` varchar(100) DEFAULT NULL COMMENT '作者',
  `title` varchar(500) DEFAULT NULL COMMENT '标题',
  `date` date DEFAULT NULL COMMENT '博客发布日期',
  `tags` varchar(255) DEFAULT NULL COMMENT '标签',
  `category` varchar(200) DEFAULT NULL COMMENT '分类',
  `view` int(11) DEFAULT NULL COMMENT '阅读人数',
  `comments` int(11) DEFAULT NULL COMMENT '评论人数',
  `copyright` int(11) DEFAULT NULL COMMENT '是否为转载文章',
  `summery` text COMMENT '文章摘要',
  `digg` int(11) DEFAULT NULL COMMENT '顶的人数',
  `bury` int(11) DEFAULT NULL COMMENT '踩的人数',
  `content` mediumtext COMMENT '文章内容',
  `type` int(11) DEFAULT NULL COMMENT '博客的类型',
  `createtime` date DEFAULT NULL COMMENT '创建日期',
  `updatetime` date DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
