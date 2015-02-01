-- phpMyAdmin SQL Dump
-- version 4.1.12
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 01. Feb 2015 um 16:51
-- Server Version: 5.6.16
-- PHP-Version: 5.5.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `jufo`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `datapoints`
--

DROP TABLE IF EXISTS `datapoints`;
CREATE TABLE IF NOT EXISTS `datapoints` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `from` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `locationlat` int(11) NOT NULL,
  `locationlong` int(11) NOT NULL,
  `battery` int(11) NOT NULL,
  `received` int(11) unsigned NOT NULL,
  `value` float NOT NULL,
  `value2` float NOT NULL,
  `receivernode` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `directsubnodesauth`
--

DROP TABLE IF EXISTS `directsubnodesauth`;
CREATE TABLE IF NOT EXISTS `directsubnodesauth` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `node` int(11) NOT NULL,
  `token` varchar(200) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `nodeconf`
--

DROP TABLE IF EXISTS `nodeconf`;
CREATE TABLE IF NOT EXISTS `nodeconf` (
  `id` int(11) unsigned NOT NULL,
  `syncinterval` bigint(11) NOT NULL,
  `superauthtoken` varchar(200) DEFAULT '',
  `name` varchar(40) DEFAULT NULL,
  `description` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `nodes`
--

DROP TABLE IF EXISTS `nodes`;
CREATE TABLE IF NOT EXISTS `nodes` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `name` varchar(30) NOT NULL DEFAULT '',
  `description` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `sensors`
--

DROP TABLE IF EXISTS `sensors`;
CREATE TABLE IF NOT EXISTS `sensors` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `node` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `sensor_gas`
--

DROP TABLE IF EXISTS `sensor_gas`;
CREATE TABLE IF NOT EXISTS `sensor_gas` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `detapoint` int(11) unsigned NOT NULL,
  `phaseshift` int(3) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `sensor_radiodose`
--

DROP TABLE IF EXISTS `sensor_radiodose`;
CREATE TABLE IF NOT EXISTS `sensor_radiodose` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `datapoint` int(11) unsigned NOT NULL,
  `dose` int(5) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL DEFAULT '',
  `pw` varchar(100) NOT NULL DEFAULT '',
  `readonly` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
