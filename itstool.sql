-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: May 17, 2017 at 02:52 PM
-- Server version: 10.1.19-MariaDB
-- PHP Version: 5.6.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `itstool`
--

-- --------------------------------------------------------

--
-- Table structure for table `ticket`
--

CREATE TABLE `ticket` (
  `ticketID` int(11) NOT NULL,
  `ticketTitle` varchar(60) NOT NULL,
  `ticketUID` int(11) NOT NULL,
  `ticketDesc` text NOT NULL,
  `ticketStatus` varchar(10) NOT NULL,
  `ticketCreated` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `ticket`
--

INSERT INTO `ticket` (`ticketID`, `ticketTitle`, `ticketUID`, `ticketDesc`, `ticketStatus`, `ticketCreated`) VALUES
(1, 'Example', 1, 'This is an example ticket.', 'Active', '2017-05-17'),
(2, 'Closed Example', 1, 'This ticket is closed.', 'Closed', '2017-05-17'),
(3, 'Request', 2, 'Help!', 'Active', '2017-05-17'),
(4, 'Old', 2, 'This ticket is old and closed.', 'Closed', '2017-05-17'),
(5, 'Filler Ticket #1', 2, 'Lorem ipsum', 'Active', '2017-05-17'),
(6, 'Filler Ticket #2', 2, 'asdasd', 'Active', '2017-05-17'),
(7, 'Filler Ticket #3', 2, 'Text \n\non\n		multiple lines.', 'Active', '2017-05-17'),
(8, 'Filler Ticket #4', 2, 'Keep going.', 'Active', '2017-05-17'),
(9, 'Filler Ticket #5', 2, 'This is way too many tickets.', 'Active', '2017-05-17'),
(10, 'Filler Ticket #6', 3, 'More filler', 'Closed', '2017-05-17'),
(11, 'Filler Ticket #7', 3, 'Even more filler.', 'Closed', '2017-05-17'),
(12, 'Filler Ticket #8', 3, 'Ugh', 'Closed', '2017-05-17'),
(13, 'Filler Ticket #9', 3, 'k', 'Active', '2017-05-17'),
(14, 'Filler Ticket #10', 3, 'I messed up the last ticket.', 'Closed', '2017-05-17'),
(15, 'TestData', 1, 'Test', 'Active', '2017-05-17'),
(16, 'More tests', 1, 'AAAAAAAAAAAAAAAAAAAAAAas', 'Active', '2017-05-17'),
(17, 'Testtesttesttest', 1, 'This might be a test?', 'Closed', '2017-05-17'),
(18, 'Just another ticket.', 1, 'Move along.', 'Closed', '2017-05-17'),
(19, 'Almost there', 1, 'Just a couple more tickets...', 'Active', '2017-05-17'),
(20, 'Help!', 1, 'Java work not!!', 'Active', '2017-05-17'),
(21, 'Yo', 1, '''Sup?', 'Active', '2017-05-17');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `userID` int(11) NOT NULL,
  `userFName` varchar(40) NOT NULL,
  `userLName` varchar(50) NOT NULL,
  `userType` varchar(10) NOT NULL,
  `userEmail` varchar(40) NOT NULL,
  `userPass` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`userID`, `userFName`, `userLName`, `userType`, `userEmail`, `userPass`) VALUES
(1, 'Admin', 'McAdmin', 'Admin', 'admin@stool.fi', 'adminpass'),
(2, 'Normal', 'User', 'User', 'user@stool.fi', 'userpass'),
(3, 'Firstname', 'Lastname', 'User', 'sec@stool.fi', 'secpass');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `ticket`
--
ALTER TABLE `ticket`
  ADD PRIMARY KEY (`ticketID`),
  ADD KEY `ticketUID` (`ticketUID`),
  ADD KEY `ticketUID_2` (`ticketUID`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`userID`),
  ADD UNIQUE KEY `userID` (`userID`),
  ADD UNIQUE KEY `userEmail` (`userEmail`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `ticket`
--
ALTER TABLE `ticket`
  MODIFY `ticketID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;
--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `userID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
