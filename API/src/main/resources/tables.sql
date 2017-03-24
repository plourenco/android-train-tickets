CREATE DATABASE IF NOT EXISTS `traintickets`;

USE `traintickets`;

/*
Drop tables
*/
set foreign_key_checks=0;

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS trains;
DROP TABLE IF EXISTS stations;
DROP TABLE IF EXISTS trips;
DROP TABLE IF EXISTS steps;
DROP TABLE IF EXISTS seats;
DROP TABLE IF EXISTS tickets;
DROP TABLE IF EXISTS bookings;

/*
Generate tables
 */
set foreign_key_checks=1;

CREATE TABLE IF NOT EXISTS `users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `role` INT NOT NULL,
PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS `trains` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `maxCapacity` INT NOT NULL,
  `description` TEXT NOT NULL,
PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS `stations` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `stationName` TEXT NOT NULL,
  `stationNumber` INT NOT NULL,
PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS `trips` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` TEXT NOT NULL,
  `direction` TEXT NOT NULL,
  `increment` TEXT NOT NULL,
  `skTrain` INT NOT NULL,
PRIMARY KEY (`id`), FOREIGN KEY(`skTrain`) REFERENCES trains(`id`))
ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS `steps` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `departureStationId` INT NOT NULL,
  `arrivalStationId` INT NOT NULL,
  `stepNumber` INT NOT NULL,
  `distance` INT NOT NULL,
  `price` REAL NOT NULL,
  `waitingTime` INT NOT NULL,
  `fkTrip` INT NOT NULL,
  `duration` INT NOT NULL,
  `departureTime` TIME NOT NULL,
  `arrivalTime` TIME NOT NULL,
PRIMARY KEY (`id`),
FOREIGN KEY(`departureStationId`) REFERENCES stations(`id`),
FOREIGN KEY(`arrivalStationId`) REFERENCES stations(`id`),
FOREIGN KEY(`fkTrip`) references trips(`id`))
ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS `seats` (
  `id` INT NOT NULL auto_increment,
  `seatNumber` VARCHAR(10) NOT NULL,
  `fkTrain` INT NOT NULL,
PRIMARY KEY(`id`),
FOREIGN KEY(`fkTrain`) REFERENCES trains(`id`))
ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS `tickets` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `uniqueId` TEXT NOT NULL,
  `departureStationId` INT NOT NULL,
  `arrivalStationId` INT NOT NULL,
  `ticketDate` DATE NOT NULL,
  `price` REAL NOT NULL,
  `purchaseDate` DATE NOT NULL,
  `fkUser` INT NOT NULL,
  `fkTrip` INT NOT NULL,
  `isUsed` tinyint,
PRIMARY KEY(`id`),
FOREIGN KEY(`departureStationId`) REFERENCES stations(`id`),
FOREIGN KEY(`arrivalStationId`) REFERENCES stations(`id`),
FOREIGN KEY(`fkTrip`) REFERENCES trips(`id`),
FOREIGN KEY(`fkUser`) REFERENCES users(`id`))
ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS `bookings` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `dateBooking` DATE NOT NULL,
  `fkSeat` INT NOT NULL,
  `fkTicket` INT NOT NULL,
  `fkTrip` INT NOT NULL,
PRIMARY KEY (`id`),
FOREIGN KEY (`fkSeat`) REFERENCES seats(`id`),
FOREIGN KEY (`fkTicket`) REFERENCES tickets (`id`),
FOREIGN KEY (`fkTrip`) REFERENCES trips (`id`))
ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS `config` (
    cfg_tag VARCHAR(50),
    cfg_value VARCHAR(100),
    PRIMARY KEY (`cfg_tag`));

insert ignore into `config` VALUES( 'db_version', '{version}');