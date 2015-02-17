-- phpMyAdmin SQL Dump
-- version 4.2.12deb2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Feb 17, 2015 at 12:59 AM
-- Server version: 5.5.42-1
-- PHP Version: 5.6.5-1

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

DROP TABLE IF EXISTS `datapoints`;
CREATE TABLE IF NOT EXISTS `datapoints` (
`id` int(11) unsigned NOT NULL,
  `from` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `locationlat` int(11) NOT NULL,
  `locationlong` int(11) NOT NULL,
  `battery` int(11) NOT NULL,
  `received` int(11) unsigned NOT NULL,
  `value` binary(100) NOT NULL,
  `receivernode` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=863 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `directsubnodesauth`
--

DROP TABLE IF EXISTS `directsubnodesauth`;
CREATE TABLE IF NOT EXISTS `directsubnodesauth` (
`id` int(11) unsigned NOT NULL,
  `node` int(11) NOT NULL,
  `token` varchar(200) NOT NULL DEFAULT ''
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `nodes`
--

DROP TABLE IF EXISTS `nodes`;
CREATE TABLE IF NOT EXISTS `nodes` (
  `uid` int(11) NOT NULL,
  `name` varchar(30) NOT NULL DEFAULT '',
  `description` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `sensors`
--

DROP TABLE IF EXISTS `sensors`;
CREATE TABLE IF NOT EXISTS `sensors` (
`id` int(11) unsigned NOT NULL,
  `uid` int(11) NOT NULL,
  `node` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `sensor_gas`
--

DROP TABLE IF EXISTS `sensor_gas`;
CREATE TABLE IF NOT EXISTS `sensor_gas` (
`id` int(11) unsigned NOT NULL,
  `detapoint` int(11) unsigned NOT NULL,
  `phaseshift` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `sensor_radiodose`
--

DROP TABLE IF EXISTS `sensor_radiodose`;
CREATE TABLE IF NOT EXISTS `sensor_radiodose` (
`id` int(11) unsigned NOT NULL,
  `datapoint` int(11) unsigned NOT NULL,
  `dose` int(5) unsigned NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
`id` int(11) unsigned NOT NULL,
  `name` varchar(20) NOT NULL DEFAULT '',
  `pw` varchar(100) NOT NULL DEFAULT '',
  `readonly` tinyint(1) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `datapoints`
--
ALTER TABLE `datapoints`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `directsubnodesauth`
--
ALTER TABLE `directsubnodesauth`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `sensors`
--
ALTER TABLE `sensors`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `sensor_gas`
--
ALTER TABLE `sensor_gas`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `sensor_radiodose`
--
ALTER TABLE `sensor_radiodose`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
 ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `datapoints`
--
ALTER TABLE `datapoints`
MODIFY `id` int(11) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=863;
--
-- AUTO_INCREMENT for table `directsubnodesauth`
--
ALTER TABLE `directsubnodesauth`
MODIFY `id` int(11) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `sensors`
--
ALTER TABLE `sensors`
MODIFY `id` int(11) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=11;
--
-- AUTO_INCREMENT for table `sensor_gas`
--
ALTER TABLE `sensor_gas`
MODIFY `id` int(11) unsigned NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `sensor_radiodose`
--
ALTER TABLE `sensor_radiodose`
MODIFY `id` int(11) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=17;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
MODIFY `id` int(11) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
