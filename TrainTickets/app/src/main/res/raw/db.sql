drop table if exists tickets;
create table tickets (id INTEGER PRIMARY KEY AUTOINCREMENT, uniqueId VARCHAR(255) UNIQUE, duration INTEGER, departureTime TIME, arrivalTime TIME, price FLOAT, ticketDate DATE, purchaseDate DATE, isUsed INTEGER, departureStation INTEGER, arrivalStation INTEGER, trip INTEGER, seat INTEGER);
insert into tickets values(-1, '2ad4b8eb-f39b-45d7-817d-9ca67c67133d', 10, '20:00:00', '20:10:00', 12.0, '2017-12-10', '2016-12-10', 0, 3, 4, 1, 1);

drop table if exists ticketsReviser;
create table ticketsReviser (id INTEGER PRIMARY KEY AUTOINCREMENT, uniqueId VARCHAR(255) UNIQUE, departureStationId INTEGER, arrivalStationId INTEGER, ticketDate DATE, isUsed INTEGER, tripId INTEGER);
insert into ticketsReviser values(-1, '2ad4b8eb-f39b-45d7-817d-9ca67c67133d', 3, 4, '2017-12-10', 0, 1);