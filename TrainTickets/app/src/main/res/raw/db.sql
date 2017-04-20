drop table if exists tickets;

create table if not exists tickets (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, uniqueId VARCHAR(255) UNIQUE, duration INTEGER, departureTime TIME, arrivalTime TIME, price FLOAT, ticketDate DATE, purchaseDate DATE, isUsed INTEGER, departureStationId INTEGER, departureStationName TEXT, arrivalStationId INTEGER, arrivalStationName TEXT, tripId INTEGER, tripDescription TEXT, tripDirection TEXT, seat VARCHAR(255));

drop table if exists ticketsReviser;

create table if not exists ticketsReviser (id INTEGER PRIMARY KEY AUTOINCREMENT, uniqueId VARCHAR(255) UNIQUE, departureStationId INTEGER, arrivalStationId INTEGER, ticketDate DATE, isUsed INTEGER, tripId INTEGER);