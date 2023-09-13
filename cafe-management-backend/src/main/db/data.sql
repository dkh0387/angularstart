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
VALUES (1, 'David Adams', '+4915126227287', 'david@luv2code.com', '126d', 'false', 'user'),
       (2, 'John Doe', '+4915126227287', 'john@luv2code.com', '126d', 'false', 'user'),
       (3, 'Ajay Rao', '+4915126227287', 'ajay@luv2code.com', '126d', 'false', 'user'),
       (4, 'Mary Public', '+4915126227287', 'mary@luv2code.com', '126d', 'false', 'user'),
       (5, 'Maxwell Dixon', '+4915126227287', 'max@luv2code.com', '126d', 'false', 'user'),
       (6, 'Denis Khaskin', '+4915126227287', 'deniskh87@gmail.com', '11235813', 'true', 'admin');

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
-- Table structure for table `authorities`
--

DROP TABLE IF EXISTS `authorities`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authorities`
(
    `id`        int(11)     NOT NULL AUTO_INCREMENT,
    `user_id`   int(11)     NOT NULL,
    `authority` varchar(50) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `authorities_user_key` (`user_id`, `authority`),
    CONSTRAINT `authorities_user_constr` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authorities`
--

LOCK
    TABLES `authorities` WRITE;
/*!40000 ALTER TABLE `authorities`
    DISABLE KEYS */;

INSERT INTO `authorities`
VALUES (1, 1, 'ROLE_EMPLOYEE'),
       (2, 2, 'ROLE_EMPLOYEE'),
       (3, 2, 'ROLE_MANAGER'),
       (4, 3, 'ROLE_EMPLOYEE'),
       (5, 6, 'ROLE_ADMIN');

/*!40000 ALTER TABLE `authorities`
    ENABLE KEYS */;
UNLOCK
    TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category`
(
    `id`   int(11)     NOT NULL AUTO_INCREMENT,
    `name` varchar(50) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK
    TABLES `category` WRITE;
/*!40000 ALTER TABLE `category`
    DISABLE KEYS */;

INSERT INTO `category`
VALUES (1, 'Starter'),
       (2, 'Main dishes'),
       (3, 'Dessert'),
       (4, 'Veggie'),
       (5, 'Supplements'),
       (6, 'Drinks');


/*!40000 ALTER TABLE `category`
    ENABLE KEYS */;
UNLOCK
    TABLES;

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product`
(
    `id`          int(11)      NOT NULL AUTO_INCREMENT,
    `category_id` int(11)      NOT NULL,
    `name`        varchar(50)  NOT NULL,
    `description` varchar(255) NOT NULL,
    `price`       float        NOT NULL,
    `status`      varchar(50)  NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT product_category_constr FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK
    TABLES `product` WRITE;
/*!40000 ALTER TABLE `product`
    DISABLE KEYS */;

INSERT INTO `product`
VALUES (1, 1, 'Small salat', 'different veggies, with dressing', 6.5, 'true');

/*!40000 ALTER TABLE `product`
    ENABLE KEYS */;
UNLOCK
    TABLES;

DROP TABLE IF EXISTS `bill`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bill`
(
    `id`              int(11)      NOT NULL AUTO_INCREMENT,
    `uuid`            varchar(255) NOT NULL,
    `name`            varchar(50)  NOT NULL,
    `email`           varchar(45)  NOT NULL,
    `contact_number`  varchar(45) DEFAULT NULL,
    `payment_method`  varchar(45)  NOT NULL,
    `total`           float        NOT NULL,
    `product_details` text         NOT NULL,
    `created_by`      varchar(45)  NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill`
--

LOCK
    TABLES `bill` WRITE;
/*!40000 ALTER TABLE `bill`
    DISABLE KEYS */;

INSERT INTO `bill`
VALUES (1, 'filename - 2498240923', 'Testbill', 'deniskh87@gmail.com', '39284203810', 'cash', 234.56,
        '"[{\"id\":18,\"name\":\"Doppio Coffee\",\"category\":\"Coffeeeeeee\",\"quantity\":\"1\",\"price\":120,\"total\":120},{\"id\":5,\"name\":\"Chocolate Frosted Doughnut\",\"category\":\"Doughnut\",\"quantity\":\"1\",\"price\":159,\"total\":159},{\"id\":18,\"name\":\"Doppio Coffee\",\"category\":\"Coffee\",\"quantity\":\"1\",\"price\":120,\"total\":120},{\"id\":5,\"name\":\"Chocolate Frosted Doughnut\",\"category\":\"Doughnut\",\"quantity\":\"1\",\"price\":159,\"total\":159}]"',
        'deniskh87@gmail.com');

/*!40000 ALTER TABLE `bill`
    ENABLE KEYS */;
UNLOCK
    TABLES;




