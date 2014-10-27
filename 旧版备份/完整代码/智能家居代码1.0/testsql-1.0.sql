-- MySQL dump 10.11
--
-- Host: localhost    Database: test
-- ------------------------------------------------------
-- Server version	5.0.45-community-nt

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` int(11) NOT NULL,
  `cmd` int(11) default '0',
  `user_id` int(11) default '0',
  `action` int(11) default NULL,
  `other` int(11) default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
INSERT INTO `message` VALUES (0,0,1,0,0),(1,1,1,1,0),(2,2,1,1,0),(3,3,1,0,0);
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stu`
--

DROP TABLE IF EXISTS `stu`;
CREATE TABLE `stu` (
  `id` int(11) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `stu`
--

LOCK TABLES `stu` WRITE;
/*!40000 ALTER TABLE `stu` DISABLE KEYS */;
INSERT INTO `stu` VALUES (13),(24),(50),(50),(10);
/*!40000 ALTER TABLE `stu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `te`
--

DROP TABLE IF EXISTS `te`;
CREATE TABLE `te` (
  `1` text NOT NULL,
  `2` text NOT NULL,
  `3` text NOT NULL,
  `4` text NOT NULL,
  `5` text NOT NULL,
  FULLTEXT KEY `1` (`1`,`2`,`3`,`4`,`5`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `te`
--

LOCK TABLES `te` WRITE;
/*!40000 ALTER TABLE `te` DISABLE KEYS */;
/*!40000 ALTER TABLE `te` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL default '0',
  `passwd` int(11) default NULL,
  `IP` tinytext,
  `port` int(11) default NULL,
  `state` int(11) default '0',
  `type` int(11) default '0',
  `count` int(11) default '0',
  `other` int(11) default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,16,'192.168.1.150',30030,0,0,0,0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-04-17 11:54:42
