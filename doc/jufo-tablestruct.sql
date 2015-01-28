-- phpMyAdmin SQL Dump
-- version 4.2.10.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 05. Jan 2015 um 19:55
-- Server Version: 5.5.40-0ubuntu0.14.04.1
-- PHP-Version: 5.5.9-1ubuntu4.5

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

CREATE TABLE IF NOT EXISTS `datapoints` (
`id` int(11) unsigned NOT NULL,
  `from` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `locationlat` int(11) NOT NULL,
  `locationlong` int(11) NOT NULL,
  `battery` int(11) NOT NULL,
  `received` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `value` binary(100) NOT NULL,
  `receivernode` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `directsubnodesauth`
--

CREATE TABLE IF NOT EXISTS `directsubnodesauth` (
`id` int(11) unsigned NOT NULL,
  `node` int(11) NOT NULL,
  `token` varchar(200) NOT NULL DEFAULT ''
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `nodeconf`
--

CREATE TABLE IF NOT EXISTS `nodeconf` (
  `id` int(11) unsigned NOT NULL,
  `syncinterval` bigint(11) NOT NULL,
  `superauthtoken` varchar(200) DEFAULT '',
  `name` varchar(40) DEFAULT NULL,
  `description` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `nodes`
--

CREATE TABLE IF NOT EXISTS `nodes` (
`id` int(11) unsigned NOT NULL,
  `uid` int(11) NOT NULL,
  `name` varchar(30) NOT NULL DEFAULT '',
  `description` text
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `sensors`
--

CREATE TABLE IF NOT EXISTS `sensors` (
`id` int(11) unsigned NOT NULL,
  `uid` int(11) NOT NULL,
  `node` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `sensortypes`
--

CREATE TABLE IF NOT EXISTS `sensortypes` (
`id` int(11) unsigned NOT NULL,
  `name` varchar(25) NOT NULL DEFAULT '',
  `description` text,
  `unitsuffix` varchar(40) NOT NULL DEFAULT ''
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `users`
--

CREATE TABLE IF NOT EXISTS `users` (
`id` int(11) unsigned NOT NULL,
  `name` varchar(20) NOT NULL DEFAULT '',
  `pw` varchar(100) NOT NULL DEFAULT '',
  `readonly` tinyint(1) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `datapoints`
--
ALTER TABLE `datapoints`
 ADD PRIMARY KEY (`id`), ADD UNIQUE KEY `from_3` (`from`,`received`,`receivernode`), ADD KEY `from_2` (`from`,`received`), ADD KEY `type` (`type`,`received`);

--
-- Indizes für die Tabelle `directsubnodesauth`
--
ALTER TABLE `directsubnodesauth`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `nodeconf`
--
ALTER TABLE `nodeconf`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `nodes`
--
ALTER TABLE `nodes`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `sensors`
--
ALTER TABLE `sensors`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `sensortypes`
--
ALTER TABLE `sensortypes`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `users`
--
ALTER TABLE `users`
 ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `datapoints`
--
ALTER TABLE `datapoints`
MODIFY `id` int(11) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=14;
--
-- AUTO_INCREMENT für Tabelle `directsubnodesauth`
--
ALTER TABLE `directsubnodesauth`
MODIFY `id` int(11) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT für Tabelle `nodes`
--
ALTER TABLE `nodes`
MODIFY `id` int(11) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT für Tabelle `sensors`
--
ALTER TABLE `sensors`
MODIFY `id` int(11) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT für Tabelle `sensortypes`
--
ALTER TABLE `sensortypes`
MODIFY `id` int(11) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT für Tabelle `users`
--
ALTER TABLE `users`
MODIFY `id` int(11) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
