-- phpMyAdmin SQL Dump
-- version 3.2.3
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 29, 2012 at 06:52 PM
-- Server version: 5.1.40
-- PHP Version: 5.3.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `mail_db`
--
CREATE DATABASE `mail_db` DEFAULT CHARACTER SET cp1251 COLLATE cp1251_general_ci;
USE `mail_db`;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(20) NOT NULL,
  `Surname` varchar(20) NOT NULL,
  `user_name` text NOT NULL,
  `password` varchar(16) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=cp1251 AUTO_INCREMENT=7 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `Name`, `Surname`, `user_name`, `password`) VALUES
(1, 'Kantoro', 'Erkulov', 'kantoro@mail.ru', '123456'),
(2, 'Akjolbek', 'Omorov', 'akjolbek@mail.ru', '123456'),
(3, 'Hasan', 'Chetin', 'hasan@mail.ru', '123456'),
(4, 'Abdurahim', 'Abdrasul uulu', 'abdurahim@mail.ru', '123456'),
(5, 'Mirbek', 'Atabekov', 'mirbek@mail.ru', '123456'),
(6, 'Azamat', 'Rahmatillaev', 'azamat@mail.ru', '123456');
