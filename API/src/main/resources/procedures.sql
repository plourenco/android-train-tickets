# Get all stations
DELIMITER //
CREATE PROCEDURE getStations()
  BEGIN
    SELECT
      id,
      stationNumber,
      stationName
    FROM stations;
  END //
DELIMITER ;


# Get schedule for the train given the DepartureStation and ArrivalStation
DELIMITER //
CREATE PROCEDURE getSchedule(IN depStation INT, IN arrStation INT)
  BEGIN
    IF (depStation < arrStation)
    THEN
      SELECT
        sch.description,
        stb.startStation,
        stb.endStation,
        sch.id,
        sch.departureStationId,
        sch.arrivalStationId,
        sch.departureTime,
        sch.arrivalTime
      FROM
        (SELECT
           tr.id,
           min(departureStationId) AS departureStationId,
           max(arrivalStationId)   AS arrivalStationId,
           max(arrivalTime)        AS arrivalTime,
           min(departureTime)      AS departureTime,
           tr.description
         FROM trips tr
           JOIN steps st ON tr.id = st.fkTrip
         WHERE departureStationId = depStation OR arrivalStationId = arrStation
         GROUP BY id) AS sch
        JOIN
        (SELECT
           p1.id          AS st1,
           p1.stationName AS startStation,
           p2.id          AS st2,
           p2.stationName AS endStation
         FROM stations p1, stations p2
         WHERE p1.id = depStation AND p2.id = arrStation) AS stb
      WHERE stb.st1 = sch.departureStationId AND stb.st2 = sch.arrivalStationId
      ORDER BY departureTime;
    ELSE
      SELECT
        stb.startStation,
        stb.endStation,
        sch.id,
        sch.departureStationId,
        sch.arrivalStationId,
        sch.departureTime,
        sch.arrivalTime
      FROM
        (SELECT
           tr.id,
           max(departureStationId) AS departureStationId,
           min(arrivalStationId)   AS arrivalStationId,
           max(arrivalTime)        AS arrivalTime,
           min(departureTime)      AS departureTime,
           tr.description
         FROM trips tr
           JOIN steps st ON tr.id = st.fkTrip
         WHERE departureStationId = depStation OR arrivalStationId = arrStation
         GROUP BY id) AS sch
        JOIN
        (SELECT
           p1.id          AS st1,
           p1.stationName AS startStation,
           p2.id          AS st2,
           p2.stationName AS endStation
         FROM stations p1, stations p2
         WHERE p1.id = depStation AND p2.id = arrStation) AS stb
      WHERE stb.st1 = sch.departureStationId AND stb.st2 = sch.arrivalStationId
      ORDER BY departureTime;
    END IF;
  END //
DELIMITER ;

# Get ticket price, travel time given the Trip, Departure station and Arrival station
DELIMITER //
CREATE PROCEDURE getFair(IN Trip INT, IN startStation INT, IN endStation INT)
  BEGIN
    IF (endStation > startStation)
    THEN
      SELECT
        steps.fkTrip,
        startStation                                       AS departureStationId,
        endStation                                         AS arrivalStationId,
        sum(steps.distance)                                AS distance,
        sum(steps.duration) + sum(steps.waitingTime) - '5' AS duration,
        min(steps.departureTime)                           AS departureTime,
        max(steps.arrivalTime)                             AS arrivalTime,
        sum(steps.price)                                   AS price
      FROM steps
      WHERE steps.departureStationId >= startStation AND
            steps.arrivalStationId <= endStation AND fkTrip = Trip;
    ELSE
      SELECT
        steps.fkTrip,
        startStation                                       AS departureStationId,
        endStation                                         AS arrivalStationId,
        sum(steps.distance)                                AS distance,
        sum(steps.duration) + sum(steps.waitingTime) - '5' AS duration,
        min(steps.departureTime)                           AS departureTime,
        max(steps.arrivalTime)                             AS arrivalTime,
        sum(steps.price)                                   AS price
      FROM steps
      WHERE steps.departureStationId <= startStation AND
            steps.arrivalStationId >= endStation AND fkTrip = Trip;
    END IF;
  END //
DELIMITER ;

# Check how many tickets have been sold for a trip  givem the Date, Trip ,Departure station and Arrival station
# availability check is done on each step for the whole trip
DELIMITER //
CREATE PROCEDURE availableTickets(IN checkDate DATE, IN startStation INT, IN endStation INT, IN tripID INT)
  BEGIN
    IF (startStation < endStation)
    THEN
      SELECT
        max(sa.sold)   AS sold,
        sa.maxCapacity AS maxCapacity
      FROM (SELECT
              count(tickets.id) AS sold,
              steps.id,
              trains.maxCapacity
            FROM tickets
              JOIN trips ON tickets.fkTrip = trips.id
              JOIN steps ON trips.id = steps.fkTrip
              JOIN trains ON trips.skTrain = trains.id
            WHERE tickets.ticketDate = checkDate AND
                  steps.departureStationId >= tickets.departureStationId AND
                  steps.arrivalStationId <= tickets.arrivalStationId AND
                  steps.arrivalStationId > steps.departureStationId
                  AND steps.departureStationId >= startStation AND steps.arrivalStationId <= endStation
                  AND tickets.fkTrip = tripId
            GROUP BY steps.id) AS sa;
    ELSE
      SELECT
        max(sa.sold)   AS sold,
        sa.maxCapacity AS maxCapacity
      FROM (SELECT
              count(tickets.idTickets) AS sold,
              steps.idSteps,
              trains.maxCapacity
            FROM tickets
              JOIN trips ON tickets.fkTrip = trips.idTrips
              JOIN steps ON trips.idTrips = steps.fkTrip
              JOIN trains ON trips.skTrain = trains.idTrains
            WHERE tickets.ticketDate = checkDate AND
                  steps.departureStationId <= tickets.departureStationId AND
                  steps.arrivalStationId >= tickets.arrivalStationId AND
                  steps.arrivalStationId < steps.departureStationId
                  AND steps.departureStationId <= startStation AND steps.arrivalStationId >= endStation
                  AND tickets.fkTrip = tripId
            GROUP BY steps.idS) AS sa;
    END IF;
  END //
DELIMITER ;

# create a new user (not controller)  given Username, Password and Email
DELIMITER //
CREATE PROCEDURE createUser(IN username TEXT, IN pass TEXT, IN email VARCHAR(255))
  BEGIN
    INSERT INTO users VALUES (NULL, username, pass, email, 1);
  END //
DELIMITER ;

# Return the Username and Role given Email and Password
DELIMITER //
CREATE PROCEDURE loginCheck(IN pass TEXT, IN email VARCHAR(255))
  BEGIN
    SELECT
      id,
      username,
      role
    FROM users
    WHERE email = email AND password = pass;
  END //
DELIMITER ;

# Returns all the tickets for a given User Id
DELIMITER //
CREATE PROCEDURE getUserTickets(IN userId INT)
  BEGIN
    SELECT
      sta.uniqueId,
      sta.id,
      sta.ticketDate,
      sta.price,
      sta.purchaseDate,
      sta.fromStation,
      sta.departureStationId,
      sta.departureTime,
      stb.toStation,
      sta.arrivalStationId,
      stb.arrivalTime,
      sta.seatNumber,
      sta.direction,
      sta.Idtrip,
      sta.isUsed
    FROM (SELECT
            tickets.uniqueId,
            tickets.id,
            tickets.ticketDate,
            stations.id          AS idDeparture,
            steps.departureTime,
            stations.stationName AS fromStation,
            seats.seatNumber,
            trips.direction,
            tickets.fkTrip       AS idTrip,
            tickets.isUsed,
            tickets.price,
            tickets.purchaseDate,
            tickets.departureStationId,
            tickets.arrivalStationId
          FROM tickets
            JOIN stations ON tickets.departureStationId = stations.id
            JOIN steps ON steps.departureStationId = stations.id
            LEFT JOIN bookings ON tickets.id = bookings.fkTicket
            LEFT JOIN seats ON bookings.fkSeat = seats.id
            JOIN trips ON tickets.fkTrip = trips.id
          WHERE tickets.fkTrip = steps.fkTrip AND tickets.fkUser = userId AND tickets.isUsed = FALSE) AS sta
      JOIN (SELECT
              tickets.uniqueId,
              tickets.id,
              stations.id          AS idArrival,
              steps.arrivalTime,
              stations.stationName AS toStation
            FROM tickets
              JOIN stations ON tickets.arrivalStationId = stations.id
              JOIN steps ON steps.arrivalStationId = stations.id
            WHERE tickets.fkTrip = steps.fkTrip AND tickets.fkUser = userId AND tickets.isUsed = FALSE) AS stb
        ON sta.id = stb.id
    ORDER BY id;
  END //
DELIMITER ;

# returns to the controller  all the tickets for a give Date and Trip
DELIMITER //
CREATE PROCEDURE getTicketsControl(IN DateCheck DATE, IN trip INT)
  BEGIN
    SELECT
      tickets.id,
      tickets.uniqueId,
      tickets.ticketDate,
      tickets.departureStationId,
      tickets.arrivalStationId
    FROM tickets
    WHERE tickets.isUsed = FALSE
          AND tickets.ticketDate = DateCheck
          AND tickets.fkTrip = trip;
  END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE getTrips()
  BEGIN
    SELECT *
    FROM trips;
  END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE buyTicket(IN uniqueUUID   TEXT, IN depStation INT, IN arrStation INT, IN ticketDate DATE,
                           IN price        FLOAT,
                           IN purchadeDate DATE, IN userId INT, IN tripID INT, IN seat VARCHAR(25))
  BEGIN
    DECLARE IDTICKET, OUTSTATION, INSTATION, INSEATID INT;
    DECLARE NRSEAT VARCHAR(25);
    IF (seat IS NULL)
    THEN
      SELECT
        seatNumber,
        seatId
      INTO NRSEAT, INSEATID
      FROM (SELECT
              seats.seatNumber,
              trips.skTrain,
              trips.direction,
              trains.maxCapacity,
              trains.description,
              trips.id AS idTrip,
              seats.id AS seatId
            FROM seats
              JOIN trains ON seats.trainId = trains.id
              JOIN trips ON trains.id = trips.skTrain
            WHERE trips.id = 4) AS sta LEFT JOIN
        (SELECT
           bookings.id AS bookingId,
           bookings.dateBooking,
           bookings.fkSeat,
           bookings.fkTicket,
           bookings.fkTrip
         FROM bookings
         WHERE bookings.fkTrip = tripID AND bookings.dateBooking = ticketDate) AS stb
          ON sta.seatId = stb.fkSeat
      WHERE bookingId IS NULL
      LIMIT 1;
      SET seat = NRSEAT;
    ELSE
      SELECT seats.id
      INTO INSEATID
      FROM seats
        JOIN trains ON seats.trainId = trains.id
        JOIN trips ON trains.id = trips.skTrain
      WHERE seats.seatNumber = seat AND trips.id = tripId;
    END IF;
    INSERT INTO tickets
    VALUES (NULL, uniqueUUID, depStation, arrStation, ticketDate, price, purchadeDate, userId, tripID, FALSE, seat);
    SELECT last_insert_id()
    INTO IDTICKET;
    INSERT INTO bookings VALUES (NULL, ticketDate, INSEATID, IDTICKET, tripID);
    SELECT
      kd.departureTime,
      kd.arrivalTime,
      ke.id,
      ke.uniqueId,
      ke.departureStationId,
      ke.arrivalStationId,
      ke.ticketDate,
      ke.price,
      ke.purchaseDate,
      ke.fkUser,
      ke.fkTrip,
      ke.isUsed,
      ke.seatNumber
    FROM (SELECT
            ka.departureTime,
            kb.arrivalTime,
            ka.fkTrip
          FROM (SELECT
                  steps.departureTime,
                  steps.fkTrip
                FROM steps
                WHERE steps.departureStationId = depStation AND steps.fkTrip = tripID) AS ka
            JOIN (SELECT
                    steps.arrivalTime,
                    steps.fkTrip
                  FROM steps
                  WHERE steps.arrivalStationId = arrStation AND steps.fkTrip = tripID) AS kb
              ON ka.fkTrip = kb.fkTrip) AS kd
      JOIN (SELECT *
            FROM tickets) AS ke ON kd.fkTrip = ke.fkTrip AND ke.id = IDTICKET;

  END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE getFirstSeat(IN trip INT, IN tripDate DATE)
  BEGIN
    SELECT
      seatNumber,
      seatId,
      bookingId
    FROM (SELECT
            seats.seatNumber,
            trips.skTrain,
            trips.direction,
            trains.maxCapacity,
            trains.description,
            trips.id AS idTrip,
            seats.id AS seatId
          FROM seats
            JOIN trains ON seats.trainId = trains.id
            JOIN trips ON trains.id = trips.skTrain
          WHERE trips.id = 4) AS sta LEFT JOIN
      (SELECT
         bookings.id AS bookingId,
         bookings.dateBooking,
         bookings.fkSeat,
         bookings.fkTicket,
         bookings.fkTrip
       FROM bookings
       WHERE bookings.fkTrip = trip AND bookings.dateBooking = tripDate) AS stb
        ON sta.seatId = stb.fkSeat
    WHERE bookingId IS NULL
    LIMIT 1;
  END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE getAvailableSeatS(IN trip INT, IN checkDate DATE)
  BEGIN
    SELECT
      seatNumber,
      seatId,
      bookingId
    FROM (SELECT
            seats.seatNumber,
            trips.skTrain,
            trips.direction,
            trains.maxCapacity,
            trains.description,
            trips.id AS idTrip,
            seats.id AS seatId
          FROM seats
            JOIN trains ON seats.trainId = trains.id
            JOIN trips ON trains.id = trips.skTrain
          WHERE trips.id = 4) AS sta LEFT JOIN
      (SELECT
         bookings.id AS bookingId,
         bookings.dateBooking,
         bookings.fkSeat,
         bookings.fkTicket,
         bookings.fkTrip
       FROM bookings
       WHERE bookings.fkTrip = trip AND bookings.dateBooking = checkDate) AS stb
        ON sta.seatId = stb.fkSeat
    WHERE bookingId IS NULL;
  END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE createBooking(IN tripDate DATE, seatId INT, IN ticketID INT, IN tripId INT)
  BEGIN
    INSERT INTO bookings VALUES (NULL, tripDate, seatId, ticketId, tripId);
  END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE createRevisor(IN username TEXT, IN pass TEXT, IN email VARCHAR(255))
  BEGIN
    INSERT INTO users VALUES (NULL, username, pass, email, 2);
  END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE setStateToUsed(IN ticketUUID TEXT)
  BEGIN
    SET SQL_SAFE_UPDATES = 0;
    UPDATE tickets
    SET isUsed = TRUE
    WHERE uniqueID = ticketUUID;
    SET SQL_SAFE_UPDATES = 1;
  END //
DELIMITER ;

# Get all timetables
DELIMITER //
CREATE PROCEDURE getTimeTable()
  BEGIN
    SELECT
      trains.id          AS idTrain,
      trains.description AS traindescription,
      trips.description  AS tripdescription,
      trips.direction    AS direction,
      trips.id           AS tripId,
      trips.increment    AS increment,
      stc.idStepDep      AS idStep,
      stc.idStationDep   AS idStationDep,
      stc.idStationArr   AS idStationArr,
      stc.stationNameDe  AS stationNameDep,
      stc.stationNameArr AS stationNameArr,
      stc.departureTime  AS departureTime,
      stc.arrivalTime    AS arrivalTime,
      stc.duration       AS duration,
      stc.waitingTime    AS waitingTime
    FROM (SELECT *
          FROM (SELECT
                  steps.id                 AS idStepDep,
                  steps.departureStationId AS idStepStationDep,
                  steps.fkTrip                fkTripDep,
                  steps.duration,
                  steps.waitingTime,
                  steps.departureTime,
                  steps.arrivalTime,
                  stations.id              AS idStationDep,
                  stations.stationName     AS stationNameDe,
                  stations.stationNumber   AS stationNumberDep
                FROM steps
                  JOIN stations ON
                                  steps.departureStationId = stations.id) AS sta
            JOIN (
                   SELECT
                     steps.id               AS idStepArr,
                     steps.arrivalStationId AS isStepStationArr,
                     steps.fkTrip           AS fkTripArr,
                     stations.id            AS idStationArr,
                     stations.stationName   AS stationNameArr,
                     stations.stationNumber AS stationNumberArr
                   FROM steps
                     JOIN stations ON
                                     steps.arrivalStationId = stations.id) AS stb
              ON sta.idStepDep = stb.IdStepArr
          ORDER BY idStepDep) AS stc
      JOIN trips ON stc.fkTripDep = trips.id
      JOIN trains ON trips.skTrain = trains.id
    ORDER BY idTrain, tripId, idStep;
  END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE getTicketsRevisor(IN direction TEXT, IN Trip TEXT, IN dateTrip DATE)
  BEGIN
    SELECT
      tickets.id     AS idTicket,
      tickets.uniqueId,
      tickets.departureStationId,
      tickets.arrivalStationId,
      tickets.ticketDate,
      tickets.isUsed,
      tickets.price,
      tickets.purchaseDate,
      tickets.fkTrip AS idTrip,
      trips.description,
      trips.direction,
      trips.increment,
      trips.skTrain  AS idTrain
    FROM tickets
      JOIN trips ON tickets.fkTrip = trips.id
    WHERE trips.direction = direction
          AND trips.description = Trip
          AND tickets.isUsed = 0
          AND tickets.ticketDate = dateTrip;
  END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE getExpiredTickets(IN userId INT)
  BEGIN
    SELECT
      sta.uniqueId,
      sta.id,
      sta.ticketDate,
      sta.price,
      sta.purchaseDate,
      sta.fromStation,
      sta.departureStationId,
      sta.departureTime,
      stb.toStation,
      sta.arrivalStationId,
      stb.arrivalTime,
      sta.seatNumber,
      sta.direction,
      sta.Idtrip,
      sta.isUsed
    FROM (SELECT
            tickets.uniqueId,
            tickets.id,
            tickets.ticketDate,
            stations.id          AS idDeparture,
            steps.departureTime,
            stations.stationName AS fromStation,
            seats.seatNumber,
            trips.direction,
            tickets.fkTrip       AS idTrip,
            tickets.isUsed,
            tickets.price,
            tickets.purchaseDate,
            tickets.departureStationId,
            tickets.arrivalStationId
          FROM tickets
            JOIN stations ON tickets.departureStationId = stations.id
            JOIN steps ON steps.departureStationId = stations.id
            LEFT JOIN bookings ON tickets.id = bookings.fkTicket
            LEFT JOIN seats ON bookings.fkSeat = seats.id
            JOIN trips ON tickets.fkTrip = trips.id
          WHERE tickets.fkTrip = steps.fkTrip AND tickets.fkUser = userId AND tickets.isUsed = TRUE) AS sta
      JOIN (SELECT
              tickets.uniqueId,
              tickets.id,
              stations.id          AS idArrival,
              steps.arrivalTime,
              stations.stationName AS toStation
            FROM tickets
              JOIN stations ON tickets.arrivalStationId = stations.id
              JOIN steps ON steps.arrivalStationId = stations.id
            WHERE tickets.fkTrip = steps.fkTrip AND tickets.fkUser = userId AND tickets.isUsed = TRUE) AS stb
        ON sta.id = stb.id
    ORDER BY ticketDate DESC
    LIMIT 10;
  END //
DELIMITER ;

# Saves the credit card. If number is the same update!
DELIMITER //
CREATE PROCEDURE saveCard(IN expiryDate DATE, IN number BIGINT, IN cvv2 INT, IN fkUser INT)
  BEGIN
    INSERT INTO creditcards (expiryDate, number, cvv2, fkUser) VALUES (expiryDate, number, cvv2, fkUser)
    ON DUPLICATE KEY UPDATE
      expiryDate = VALUES(expiryDate),
      number     = VALUES(number),
      cvv2       = VALUES(cvv2),
      fkUser     = VALUES(fkUser);
  END //
DELIMITER ;