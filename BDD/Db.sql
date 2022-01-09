-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jun 06, 2021 at 07:34 PM
-- Server version: 10.3.16-MariaDB
-- PHP Version: 7.3.23

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

--
-- Database: `id16713991_project`
--

-- --------------------------------------------------------

--
-- Table structure for table `Clients`
--

CREATE TABLE `Clients` (
  `clientID` int(10) UNSIGNED NOT NULL,
  `sessionToken` varchar(32) DEFAULT NULL,
  `fName` varchar(100) DEFAULT NULL,
  `lName` varchar(100) DEFAULT NULL,
  `birthDate` timestamp NOT NULL DEFAULT '1980-08-03 00:00:00',
  `photo` varchar(50) DEFAULT NULL,
  `identityDoc` enum('CINE','EPORT','SEJOUR','') DEFAULT NULL,
  `identityNumber` varchar(30) DEFAULT NULL,
  `inscriptionDate` timestamp NOT NULL DEFAULT current_timestamp(),
  `ensurenceValidity` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `licenceValidity` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `clientEmail` varchar(100) DEFAULT NULL,
  `passwd` varchar(100) DEFAULT NULL,
  `clientPhone` varchar(15) DEFAULT NULL,
  `priceRate` smallint(5) UNSIGNED DEFAULT NULL,
  `isActive` tinyint(1) NOT NULL DEFAULT 1,
  `notes` mediumtext DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Clients`
--

INSERT INTO `Clients` (`clientID`, `sessionToken`, `fName`, `lName`, `birthDate`, `photo`, `identityDoc`, `identityNumber`, `inscriptionDate`, `ensurenceValidity`, `licenceValidity`, `clientEmail`, `passwd`, `clientPhone`, `priceRate`, `isActive`, `notes`) VALUES
(1, NULL, 'mohammed', 'bellamine', '2000-09-13 00:00:00', 'Mohammed-BELLAMINE-1.jpeg', 'CINE', 'AB15243', '2020-09-12 23:00:00', '2021-09-12 00:00:00', '0000-00-00 00:00:00', 'mohammed.bellamine@gmail.com', '000000', '066666988', 100, 1, ''),
(4, NULL, 'Hassan', 'Abouabdellah', '1992-09-13 00:00:00', 'Hassan-Abouabdellah-2.jpeg', 'CINE', 'AB213636', '2020-09-13 07:13:30', '2020-11-15 00:00:00', '0000-00-00 00:00:00', 'hassan.abouabdellah@gmail.com', '000000', '+212661661661', 100, 1, ''),
(6, NULL, 'Ayoub', 'BELKHADIR', '1988-09-13 00:00:00', 'default.jpg', 'CINE', 'AB213636', '2020-09-13 07:13:30', '2020-11-15 00:00:00', '0000-00-00 00:00:00', 'mohammed.belatar@gmail.com', '', '+212661661661', 100, 1, ''),
(5, NULL, 'Hassan', 'RAHMOUNI', '1982-09-13 09:54:15', 'default.jpg', 'CINE', 'AB15243', '2020-09-13 07:13:30', '2021-09-12 23:00:00', '0000-00-00 00:00:00', 'mohammed.belatar@gmail.com', '', '+212661661661', 100, 1, ''),
(7, NULL, 'Sofia', 'KAMALI', '1990-09-13 00:00:00', 'Sofia-KAMALI-7.jpeg', 'CINE', 'AB213636', '2020-09-13 07:13:30', '2020-11-15 00:00:00', '0000-00-00 00:00:00', 'mohammed.belatar@gmail.com', '', '+212661661661', 100, 1, ''),
(8, NULL, 'Hanae', 'BENZINE', '1992-09-13 09:54:15', 'default.jpg', 'CINE', 'AB15243', '2020-09-13 07:13:30', '2021-09-12 23:00:00', '0000-00-00 00:00:00', 'mohammed.belatar@gmail.com', '', '+212661661661', 100, 1, ''),
(9, NULL, 'Fadwa', 'KOUROUKOU', '1992-09-13 09:54:15', 'default.jpg', 'CINE', 'AB15243', '2020-09-13 07:13:30', '2021-09-12 23:00:00', '0000-00-00 00:00:00', 'mohammed.belatar@gmail.com', '', '+212661661661', 100, 1, ''),
(10, NULL, 'tareq', 'sss', '1992-09-19 00:00:00', 'default.jpg', '', 'AB15243sss', '2020-09-13 11:40:43', '2020-09-18 23:00:00', '2020-10-31 00:00:00', 'mohammed.belatar@gmail.com', '', '+212661661661', 1001, 1, 'sz'),
(14, NULL, 'Salah Eddine', 'Chennifa', '1980-08-03 00:00:00', NULL, NULL, NULL, '2021-06-03 23:55:00', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 'salah@gmail.com', '000000', '0666698', NULL, 1, NULL),
(15, NULL, 'TEST_FNAME', 'TEST_LAST_NAME', '1980-08-03 00:00:00', NULL, NULL, NULL, '2021-06-06 19:16:20', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 'TEST_email@gmail.com', '000000', '06668899', NULL, 1, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `Seances`
--

CREATE TABLE `Seances` (
  `seanceID` int(10) UNSIGNED NOT NULL,
  `seanceGrpID` int(10) UNSIGNED DEFAULT NULL,
  `clientID` int(10) UNSIGNED NOT NULL,
  `monitorID` smallint(5) UNSIGNED NOT NULL,
  `startDate` timestamp NOT NULL DEFAULT current_timestamp(),
  `durationMinut` tinyint(3) UNSIGNED NOT NULL DEFAULT 120,
  `isDone` tinyint(1) NOT NULL DEFAULT 0,
  `paymentID` int(10) UNSIGNED DEFAULT 0,
  `comments` varchar(200) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Seances`
--

INSERT INTO `Seances` (`seanceID`, `seanceGrpID`, `clientID`, `monitorID`, `startDate`, `durationMinut`, `isDone`, `paymentID`, `comments`) VALUES
(1, 1, 1, 2, '2021-06-06 09:00:00', 120, 0, 1, ''),
(2, 1, 2, 2, '2021-05-19 09:00:00', 120, 0, 0, ''),
(3, 1, 6, 2, '2021-06-06 09:00:00', 120, 1, 0, ''),
(4, 1, 4, 8, '2021-09-14 08:00:00', 60, 0, 0, ''),
(5, 1, 1, 2, '2021-05-27 14:00:00', 120, 0, 1, ''),
(6, 1, 1, 2, '2021-06-05 09:00:00', 120, 1, 0, ''),
(158, 2, 2, 2, '2020-10-26 09:00:00', 150, 0, 0, ''),
(157, 2, 2, 2, '2020-10-19 09:00:00', 150, 0, 0, ''),
(156, 2, 2, 2, '2020-10-12 09:00:00', 150, 0, 0, ''),
(155, 2, 2, 2, '2020-10-05 09:00:00', 150, 0, 0, ''),
(159, 2, 8, 2, '2020-10-05 09:00:00', 150, 0, 0, ''),
(160, 2, 8, 2, '2021-06-06 09:00:00', 150, 1, 0, ''),
(161, 2, 8, 2, '2020-10-19 09:00:00', 150, 0, 0, ''),
(162, 2, 8, 2, '2020-10-26 09:00:00', 150, 0, 0, ''),
(163, 2, 9, 2, '2020-10-05 09:00:00', 150, 0, 0, ''),
(164, 2, 9, 2, '2020-10-12 09:00:00', 150, 0, 0, ''),
(165, 2, 9, 2, '2020-10-19 09:00:00', 150, 0, 0, ''),
(166, 2, 9, 2, '2020-10-26 09:00:00', 150, 0, 0, ''),
(167, 3, 2, 4, '2020-10-17 09:00:00', 120, 0, 0, ''),
(168, 3, 2, 4, '2020-10-24 09:00:00', 120, 0, 0, ''),
(169, 3, 2, 4, '2020-10-30 10:00:00', 120, 1, 0, ''),
(170, 3, 2, 4, '2020-11-06 10:00:00', 120, 0, 0, ''),
(171, 3, 2, 4, '2020-11-13 10:00:00', 120, 0, 0, ''),
(172, 3, 8, 4, '2020-10-17 09:00:00', 120, 0, 0, ''),
(173, 3, 8, 4, '2020-10-24 09:00:00', 120, 0, 0, ''),
(174, 3, 8, 4, '2020-10-30 10:00:00', 120, 1, 0, ''),
(175, 3, 8, 4, '2020-11-06 10:00:00', 120, 0, 0, ''),
(176, 3, 8, 4, '2020-11-13 10:00:00', 120, 0, 0, ''),
(177, 3, 9, 4, '2020-10-17 09:00:00', 120, 0, 0, ''),
(178, 3, 9, 4, '2020-10-24 09:00:00', 120, 0, 0, ''),
(179, 3, 9, 4, '2020-10-30 10:00:00', 120, 0, 0, ''),
(180, 3, 9, 4, '2020-11-06 10:00:00', 120, 0, 0, ''),
(181, 3, 9, 4, '2020-11-13 10:00:00', 120, 0, 0, ''),
(182, 4, 6, 2, '2020-10-31 14:00:00', 120, 0, 0, ''),
(183, 4, 6, 2, '2020-11-07 14:00:00', 120, 0, 0, ''),
(184, 4, 6, 2, '2020-11-14 14:00:00', 120, 0, 0, ''),
(185, 4, 6, 2, '2020-11-21 14:00:00', 120, 0, 0, ''),
(186, 4, 6, 2, '2020-11-28 14:00:00', 120, 0, 0, ''),
(187, 4, 1, 2, '2021-06-07 14:00:00', 120, 0, 0, ''),
(188, 4, 1, 2, '2021-05-10 11:00:00', 120, 0, 0, ''),
(189, 4, 1, 2, '2021-04-30 12:00:00', 120, 0, 0, ''),
(190, 4, 1, 2, '2021-06-08 14:00:00', 120, 0, 0, ''),
(191, 4, 1, 2, '2021-07-01 14:00:00', 120, 0, 0, ''),
(192, 4, 7, 2, '2020-10-31 14:00:00', 120, 0, 0, ''),
(193, 4, 7, 2, '2020-11-07 14:00:00', 120, 0, 0, ''),
(194, 4, 7, 2, '2020-11-14 14:00:00', 120, 0, 0, ''),
(195, 4, 7, 2, '2020-11-21 14:00:00', 120, 0, 0, ''),
(196, 4, 7, 2, '2020-11-28 14:00:00', 120, 0, 0, ''),
(197, 4, 5, 2, '2020-10-31 14:00:00', 120, 0, 0, ''),
(198, 4, 5, 2, '2020-11-07 14:00:00', 120, 0, 0, ''),
(199, 4, 5, 2, '2020-11-14 14:00:00', 120, 0, 0, ''),
(200, 4, 5, 2, '2020-11-21 14:00:00', 120, 0, 0, ''),
(201, 4, 5, 2, '2020-11-28 14:00:00', 120, 0, 0, ''),
(202, 5, 6, 6, '2020-11-01 10:00:00', 120, 0, 0, ''),
(203, 5, 6, 6, '2020-11-08 10:00:00', 120, 0, 0, ''),
(204, 5, 6, 6, '2020-11-15 10:00:00', 120, 0, 0, ''),
(205, 5, 6, 6, '2020-11-22 10:00:00', 120, 0, 0, ''),
(206, 5, 1, 6, '2020-11-01 10:00:00', 120, 0, 0, ''),
(207, 5, 1, 6, '2020-11-08 10:00:00', 120, 0, 0, ''),
(208, 5, 1, 6, '2020-11-15 10:00:00', 120, 0, 0, ''),
(209, 5, 1, 6, '2020-11-22 10:00:00', 120, 0, 0, ''),
(210, 5, 7, 6, '2020-11-01 10:00:00', 120, 0, 0, ''),
(211, 5, 7, 6, '2020-11-08 10:00:00', 120, 0, 0, ''),
(212, 5, 7, 6, '2020-11-15 10:00:00', 120, 0, 0, ''),
(213, 5, 7, 6, '2020-11-22 10:00:00', 120, 0, 0, ''),
(214, 6, 6, 6, '2020-10-31 10:00:00', 120, 0, 0, ''),
(215, 6, 6, 6, '2020-11-07 10:00:00', 120, 0, 0, ''),
(216, 6, 6, 6, '2020-11-14 10:00:00', 120, 0, 0, ''),
(217, 6, 6, 6, '2020-11-21 10:00:00', 120, 0, 0, ''),
(218, 6, 1, 6, '2020-10-31 10:00:00', 120, 0, 0, ''),
(219, 6, 1, 6, '2020-11-07 10:00:00', 120, 0, 0, ''),
(220, 6, 1, 6, '2020-11-14 10:00:00', 120, 0, 0, ''),
(221, 6, 1, 6, '2020-11-21 10:00:00', 120, 0, 0, ''),
(222, 7, 2, 2, '2020-12-19 14:00:00', 120, 1, 0, ''),
(223, 7, 2, 2, '2020-12-26 14:00:00', 120, 0, 0, ''),
(224, 7, 2, 2, '2021-01-02 14:00:00', 120, 0, 0, ''),
(225, 7, 2, 2, '2021-01-09 14:00:00', 120, 0, 0, ''),
(226, 7, 8, 2, '2020-12-19 14:00:00', 120, 0, 0, ''),
(227, 7, 8, 2, '2020-12-26 14:00:00', 120, 0, 0, ''),
(228, 7, 8, 2, '2021-01-02 14:00:00', 120, 0, 0, ''),
(229, 7, 8, 2, '2021-01-09 14:00:00', 120, 0, 0, ''),
(230, 7, 9, 2, '2020-12-19 14:00:00', 120, 1, 0, ''),
(231, 7, 9, 2, '2020-12-26 14:00:00', 120, 0, 0, ''),
(232, 7, 9, 2, '2021-01-02 14:00:00', 120, 0, 0, ''),
(233, 7, 9, 2, '2021-01-09 14:00:00', 120, 0, 0, ''),
(234, 8, 6, 4, '2020-12-21 14:00:00', 120, 0, 0, ''),
(235, 8, 6, 4, '2020-12-28 14:00:00', 120, 0, 0, ''),
(236, 8, 6, 4, '2021-01-04 14:00:00', 120, 0, 0, ''),
(237, 8, 1, 4, '2020-12-21 14:00:00', 120, 0, 0, ''),
(238, 8, 1, 4, '2020-12-28 14:00:00', 120, 0, 0, ''),
(239, 8, 1, 4, '2021-01-04 14:00:00', 120, 0, 0, ''),
(240, 9, 8, 4, '2020-12-25 09:00:00', 120, 0, 0, ''),
(241, 9, 8, 4, '2021-01-01 09:00:00', 120, 0, 0, ''),
(242, 9, 8, 4, '2021-01-08 09:00:00', 120, 0, 0, ''),
(243, 9, 9, 4, '2020-12-25 09:00:00', 120, 0, 0, ''),
(244, 9, 9, 4, '2021-01-01 09:00:00', 120, 0, 0, ''),
(245, 9, 9, 4, '2021-01-08 09:00:00', 120, 0, 0, ''),
(246, 10, 6, 2, '2021-01-07 09:15:00', 120, 0, 0, ''),
(247, 11, 2, 6, '2021-01-03 09:00:00', 120, 0, 0, ''),
(248, 11, 2, 6, '2021-01-10 09:00:00', 120, 0, 0, ''),
(249, 11, 2, 6, '2021-01-17 09:00:00', 120, 0, 0, ''),
(250, 11, 2, 6, '2021-01-24 09:00:00', 120, 0, 0, ''),
(251, 11, 2, 6, '2021-01-31 09:00:00', 120, 0, 0, ''),
(252, 11, 2, 6, '2021-02-07 09:00:00', 120, 0, 0, ''),
(253, 11, 2, 6, '2021-02-14 09:00:00', 120, 0, 0, ''),
(254, 11, 8, 6, '2021-01-03 09:00:00', 120, 0, 0, ''),
(255, 11, 8, 6, '2021-01-10 09:00:00', 120, 0, 0, ''),
(256, 11, 8, 6, '2021-01-17 09:00:00', 120, 0, 0, ''),
(257, 11, 8, 6, '2021-01-24 09:00:00', 120, 0, 0, ''),
(258, 11, 8, 6, '2021-01-31 09:00:00', 120, 0, 0, ''),
(259, 11, 8, 6, '2021-02-07 09:00:00', 120, 0, 0, ''),
(260, 11, 8, 6, '2021-02-14 09:00:00', 120, 0, 0, ''),
(261, 12, 2, 2, '2021-07-16 10:00:00', 120, 1, 0, ''),
(262, 12, 2, 2, '2021-07-30 12:00:00', 120, 0, 0, ''),
(263, 12, 2, 2, '2021-08-06 12:00:00', 120, 0, 0, ''),
(264, 12, 2, 2, '2021-08-13 12:00:00', 120, 0, 0, ''),
(265, 12, 10, 2, '2021-07-16 10:00:00', 120, 1, 0, ''),
(266, 12, 10, 2, '2021-07-30 12:00:00', 120, 0, 0, ''),
(267, 12, 10, 2, '2021-08-06 12:00:00', 120, 0, 0, ''),
(268, 12, 10, 2, '2021-08-13 12:00:00', 120, 0, 0, ''),
(269, 12, 6, 2, '2021-07-16 10:00:00', 120, 1, 0, ''),
(270, 12, 6, 2, '2021-07-30 12:00:00', 120, 0, 0, ''),
(271, 12, 6, 2, '2021-08-06 12:00:00', 120, 0, 0, ''),
(272, 12, 6, 2, '2021-08-13 12:00:00', 120, 0, 0, ''),
(273, NULL, 12, 11, '2021-06-04 00:00:00', 60, 0, 0, NULL),
(274, NULL, 1, 2, '2021-06-08 00:00:00', 30, 0, 0, NULL),
(277, NULL, 1, 2, '2021-06-09 08:00:00', 120, 0, 0, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `Tasks`
--

CREATE TABLE `Tasks` (
  `taskID` int(10) UNSIGNED NOT NULL,
  `startDate` timestamp NOT NULL DEFAULT current_timestamp(),
  `durationMinut` tinyint(3) UNSIGNED NOT NULL DEFAULT 60,
  `title` varchar(100) DEFAULT NULL,
  `detail` varchar(250) DEFAULT NULL,
  `isDone` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `user_Fk` smallint(5) UNSIGNED NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Tasks`
--

INSERT INTO `Tasks` (`taskID`, `startDate`, `durationMinut`, `title`, `detail`, `isDone`, `user_Fk`) VALUES
(1, '2021-06-07 13:30:00', 120, 'Longer chevaux test', 'Sortir la jument Chehrazad pour la faire travailler aux obstacles de 1m30', '0000-00-00 00:00:00', 2),
(2, '2021-06-06 15:00:00', 60, 'Longer chevaux', 'Sortir la jument Chehrazad pour la faire travailler aux obstacles de 1m30', '0000-00-00 00:00:00', 5),
(3, '2021-06-07 15:30:00', 120, 'Travailler cheveaux', 'Monter et travailler les chevaux qui travaillent peu', '0000-00-00 00:00:00', 2),
(4, '2021-06-04 13:30:00', 240, 'Travailler cheveaux', 'Monter et travailler les chevaux qui travaillent peu', '0000-00-00 00:00:00', 2),
(5, '2021-06-06 13:30:00', 240, 'Travailler cheveaux', 'Monter et travailler les chevaux qui travaillent peu', '0000-00-00 00:00:00', 2),
(6, '2020-11-20 13:30:00', 240, 'Travailler cheveaux', 'Monter et travailler les chevaux qui travaillent peu', '0000-00-00 00:00:00', 2),
(7, '2020-11-27 13:30:00', 240, 'Travailler cheveaux', 'Monter et travailler les chevaux qui travaillent peu', '0000-00-00 00:00:00', 2),
(8, '2020-12-04 13:30:00', 240, 'Travailler cheveaux', 'Monter et travailler les chevaux qui travaillent peu', '0000-00-00 00:00:00', 2),
(9, '2020-12-11 13:30:00', 240, 'Travailler cheveaux', 'Monter et travailler les chevaux qui travaillent peu', '0000-00-00 00:00:00', 2),
(10, '2020-12-18 13:30:00', 240, 'Travailler cheveaux', 'Monter et travailler les chevaux qui travaillent peu', '0000-00-00 00:00:00', 2),
(11, '2020-12-25 13:30:00', 240, 'Travailler cheveaux', 'Monter et travailler les chevaux qui travaillent peu', '0000-00-00 00:00:00', 3),
(12, '2021-01-01 13:30:00', 240, 'Travailler cheveaux', 'Monter et travailler les chevaux qui travaillent peu', '0000-00-00 00:00:00', 2),
(13, '2021-06-03 10:30:00', 120, 'debourrage', 'débourrage des juments 1,2,,3...', '0000-00-00 00:00:00', 2),
(14, '2021-08-01 10:30:00', 120, 'debourrage', 'débourrage des juments 1,2,,3...', '0000-00-00 00:00:00', 2),
(15, '2021-08-08 10:30:00', 120, 'debourrage', 'débourrage des juments 1,2,,3...', '0000-00-00 00:00:00', 2),
(16, '2021-08-15 10:30:00', 120, 'debourrage', 'débourrage des juments 1,2,,3...', '0000-00-00 00:00:00', 2),
(22, '2021-06-11 14:00:00', 90, 'TEST_TASk', NULL, '0000-00-00 00:00:00', 2),
(23, '2021-06-12 15:30:00', 90, 'TEST_TASK 1', NULL, '0000-00-00 00:00:00', 2);

-- --------------------------------------------------------

--
-- Table structure for table `User`
--

CREATE TABLE `User` (
  `userID` smallint(5) UNSIGNED NOT NULL,
  `sessionToken` varchar(32) DEFAULT NULL,
  `userEmail` varchar(100) DEFAULT NULL,
  `userPasswd` varchar(100) DEFAULT NULL,
  `adminLevel` tinyint(3) UNSIGNED DEFAULT NULL,
  `lastLoginTime` timestamp NOT NULL DEFAULT current_timestamp(),
  `isActive` tinyint(1) NOT NULL DEFAULT 1,
  `userFName` varchar(100) NOT NULL,
  `userLName` varchar(100) NOT NULL,
  `description` varchar(100) NOT NULL,
  `userType` enum('ADMIN','MONITOR','GUARD','SERVICE','OTHER','COMPTA') NOT NULL,
  `userphoto` varchar(50) NOT NULL DEFAULT 'default.jpg',
  `contractDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `userPhone` varchar(15) NOT NULL,
  `displayColor` varchar(7) NOT NULL DEFAULT '#0000FF'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `User`
--

INSERT INTO `User` (`userID`, `sessionToken`, `userEmail`, `userPasswd`, `adminLevel`, `lastLoginTime`, `isActive`, `userFName`, `userLName`, `description`, `userType`, `userphoto`, `contractDate`, `userPhone`, `displayColor`) VALUES
(1, NULL, 'mohammed.belatar@gmail.com', '000000', 100, '2019-10-11 00:39:06', 1, 'Mohammed', 'BELATAR', 'Mohammed BELATAR', 'ADMIN', 'Mohammed-BELATAR-1.png', '2018-09-18 23:00:00', '+212661661661', '#280300'),
(2, NULL, 'it@t-t.ma', '000000', 100, '2018-09-12 13:33:16', 1, 'Hassan', 'Chennofi', 'Mohammed BELATAR', 'MONITOR', 'default.jpg', '2018-09-11 18:46:02', '+212661661688', '#0000FF'),
(6, NULL, 'mouna@t-t.ma', '000000', 100, '2018-11-03 19:02:31', 1, 'Mouna', 'BENKHRABA', '', 'ADMIN', 'default.jpg', '2018-11-01 00:00:00', '+212661661661', '#ba00a2'),
(3, NULL, 'Ihsane.LAALIOUI@gmail.com', '000000', NULL, '2021-06-06 17:33:47', 1, 'Ihsane', 'Laalioui', '', 'MONITOR', 'default.jpg', '0000-00-00 00:00:00', '06666061588', '#0000FF');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Clients`
--
ALTER TABLE `Clients`
  ADD PRIMARY KEY (`clientID`);

--
-- Indexes for table `Seances`
--
ALTER TABLE `Seances`
  ADD PRIMARY KEY (`seanceID`),
  ADD KEY `userID_FKY` (`monitorID`),
  ADD KEY `clientID` (`clientID`),
  ADD KEY `startDate` (`startDate`);

--
-- Indexes for table `Tasks`
--
ALTER TABLE `Tasks`
  ADD PRIMARY KEY (`taskID`),
  ADD KEY `user_Fk` (`user_Fk`);

--
-- Indexes for table `User`
--
ALTER TABLE `User`
  ADD PRIMARY KEY (`userID`),
  ADD UNIQUE KEY `userEmail` (`userEmail`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Clients`
--
ALTER TABLE `Clients`
  MODIFY `clientID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `Seances`
--
ALTER TABLE `Seances`
  MODIFY `seanceID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=278;

--
-- AUTO_INCREMENT for table `Tasks`
--
ALTER TABLE `Tasks`
  MODIFY `taskID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT for table `User`
--
ALTER TABLE `User`
  MODIFY `userID` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
COMMIT;
