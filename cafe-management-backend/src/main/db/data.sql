CREATE DATABASE IF NOT EXISTS `cafe_db` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `cafe_db`;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user`
(
    `id`             int(11) NOT NULL AUTO_INCREMENT,
    `name`           varchar(45) DEFAULT NULL,
    `contact_number` varchar(45) DEFAULT NULL,
    `email`          varchar(45) DEFAULT NULL,
    `password`       varchar(45) DEFAULT NULL,
    `status`         varchar(20) DEFAULT NULL,
    `role`           varchar(20) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK
    TABLES `user` WRITE;
/*!40000 ALTER TABLE `user`
    DISABLE KEYS */;

INSERT INTO `user`
VALUES (1, 'David Adams', '+4915126227287', 'david@luv2code.com', '126d', 'active', 'user'),
       (2, 'John Doe', '+4915126227287', 'john@luv2code.com', '126d', 'active', 'user'),
       (3, 'Ajay Rao', '+4915126227287', 'ajay@luv2code.com', '126d', 'active', 'user'),
       (4, 'Mary Public', '+4915126227287', 'mary@luv2code.com', '126d', 'active', 'user'),
       (5, 'Maxwell Dixon', '+4915126227287', 'max@luv2code.com', '126d', 'active', 'user'),
       (6, 'Denis Khaskin', '+4915126227287', 'deniskh87@gmail.com', '11235813', 'active', 'admin');

/*!40000 ALTER TABLE `user`
    ENABLE KEYS */;
UNLOCK
    TABLES;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;