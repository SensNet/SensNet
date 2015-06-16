-- phpMyAdmin SQL Dump
-- version 4.0.10deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jun 15, 2015 at 11:25 PM
-- Server version: 5.5.43-0ubuntu0.14.04.1
-- PHP Version: 5.5.9-1ubuntu4.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `sensnet`
--

-- --------------------------------------------------------

--
-- Table structure for table `datapoints`
--

CREATE TABLE IF NOT EXISTS `datapoints` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `from` int(11) NOT NULL,
  `class` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `locationlat` int(11) NOT NULL,
  `locationlong` int(11) NOT NULL,
  `battery` int(11) NOT NULL,
  `received` int(15) unsigned NOT NULL,
  `value` binary(100) NOT NULL,
  `receivernode` int(11) NOT NULL,
  `temperature` int(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `directsubnodesauth`
--

CREATE TABLE IF NOT EXISTS `directsubnodesauth` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `node` int(11) NOT NULL,
  `token` varchar(200) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=7 ;

-- --------------------------------------------------------

--
-- Table structure for table `nodes`
--

CREATE TABLE IF NOT EXISTS `nodes` (
  `uid` int(11) NOT NULL,
  `name` varchar(30) NOT NULL DEFAULT '',
  `description` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `sensors`
--

CREATE TABLE IF NOT EXISTS `sensors` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `node` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `sensor_chemgas`
--

CREATE TABLE IF NOT EXISTS `sensor_chemgas` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `datapoint` int(11) unsigned NOT NULL,
  `voltage` float NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `sensor_gas`
--

CREATE TABLE IF NOT EXISTS `sensor_gas` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `datapoint` int(11) unsigned NOT NULL,
  `phaseshift` int(3) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `sensor_particle`
--

CREATE TABLE IF NOT EXISTS `sensor_particle` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `datapoint` int(11) unsigned NOT NULL,
  `rough_particle` int(11) unsigned NOT NULL,
  `fine_particle` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `sensor_radiodose`
--

CREATE TABLE IF NOT EXISTS `sensor_radiodose` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `datapoint` int(11) unsigned NOT NULL,
  `dose` int(5) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL DEFAULT '',
  `pw` varchar(100) NOT NULL DEFAULT '',
  `readonly` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
