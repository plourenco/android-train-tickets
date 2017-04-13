# Get all stations
DELIMITER //
create procedure getStations()
  begin
    select id, stationNumber, stationName from stations;
  end //
DELIMITER ;


# Get schedule for the train given the DepartureStation and ArrivalStation
DELIMITER //
create procedure getSchedule(IN depStation INT,IN arrStation INT)
  begin
    if(depStation<arrStation) then
      select stb.startStation,stb.endStation,sch.id,sch.departureStationId,sch.arrivalStationId,sch.departureTime,sch.arrivalTime
      from
        (select tr.id,
           min(departureStationId) as departureStationId,
           max(arrivalStationId)as arrivalStationId,
           max(arrivalTime) as arrivalTime,
           min(departureTime)as departureTime
         from trips tr join steps st on tr.id=st.fkTrip
         where departureStationId=depStation or arrivalStationId=arrStation group by id) as sch
        join
        (select p1.id as st1,
                p1.stationName as startStation,
                p2.id as st2,
                p2.stationName as endStation
         from stations p1,stations p2 where p1.id=depStation and p2.id=arrStation) as stb
      where stb.st1=sch.departureStationId and stb.st2=sch.arrivalStationId order by departureTime;
    else
      select stb.startStation,stb.endStation,sch.id,sch.departureStationId,sch.arrivalStationId,sch.departureTime,sch.arrivalTime
      from
        (select tr.id,
           max(departureStationId) as departureStationId,
           min(arrivalStationId)as arrivalStationId,
           max(arrivalTime) as arrivalTime,
           min(departureTime)as departureTime
         from trips tr join steps st on tr.id=st.fkTrip
         where departureStationId=depStation or arrivalStationId=arrStation group by id) as sch
        join
        (select p1.id as st1,
                p1.stationName as startStation,
                p2.id as st2,
                p2.stationName as endStation
         from stations p1,stations p2 where p1.id=depStation and p2.id=arrStation) as stb
      where stb.st1=sch.departureStationId and stb.st2=sch.arrivalStationId order by departureTime;
    end if;
  end //
DELIMITER ;

# Get ticket price, travel time given the Trip, Departure station and Arrival station
DELIMITER //
create procedure getFair(IN Trip INT, IN startStation INT,IN endStation INT)
  begin
    if(endStation>startStation) then
      select steps.fkTrip,
        startStation as departureStationId,
        endStation as arrivalStationId,
        sum(steps.distance) as distance,
        sum(steps.duration)+sum(steps.waitingTime)-'5' as duration,
        min(steps.departureTime) as departureTime,
        max(steps.arrivalTime) as arrivalTime,
        sum(steps.price) as price from steps
      where steps.departureStationId>=startStation and
            steps.arrivalStationId<=endStation and fkTrip=Trip;
    else
      select steps.fkTrip,
        startStation as departureStationId,
        endStation as arrivalStationId,
        sum(steps.distance) as distance,
        sum(steps.duration)+sum(steps.waitingTime)-'5' as duration,
        min(steps.departureTime) as departureTime,
        max(steps.arrivalTime) as arrivalTime,
        sum(steps.price) as price from steps
      where steps.departureStationId<=startStation and
            steps.arrivalStationId>=endStation and fkTrip=Trip;
    end if;
  end //
DELIMITER ;

# Check how many tickets have been sold for a trip  givem the Date, Trip ,Departure station and Arrival station
# availability check is done on each step for the whole trip
DELIMITER //
create procedure availableTickets(IN checkDate DATE,IN startStation INT, IN endStation INT, IN tripID INT)
  begin
    if(startStation<endStation) THEN
      select max(sa.sold) as sold,
             sa.maxCapacity  as maxCapacity
      from(select count(tickets.id)as sold,steps.id,trains.maxCapacity
           from tickets join trips on tickets.fkTrip=trips.id
             join steps on trips.id=steps.fkTrip
             join trains on trips.skTrain=trains.id
           where tickets.ticketDate=checkDate and
                 steps.departureStationId>=tickets.departureStationId and
                 steps.arrivalStationId<=tickets.arrivalStationId and steps.arrivalStationId>steps.departureStationId
                 and steps.departureStationId>=startStation and steps.arrivalStationId<=endStation
                 and tickets.fkTrip=tripId
           group by steps.id) as sa;
    ELSE
      select max(sa.sold) as sold,
             sa.maxCapacity  as maxCapacity
      from(select count(tickets.idTickets)as sold,steps.idSteps,trains.maxCapacity
           from tickets join trips on tickets.fkTrip=trips.idTrips
             join steps on trips.idTrips=steps.fkTrip
             join trains on trips.skTrain=trains.idTrains
           where tickets.ticketDate=checkDate and
                 steps.departureStationId<=tickets.departureStationId and
                 steps.arrivalStationId>=tickets.arrivalStationId and steps.arrivalStationId<steps.departureStationId
                 and steps.departureStationId<=startStation and steps.arrivalStationId>=endStation
                 and tickets.fkTrip=tripId
           group by steps.idS) as sa;
    end if;
  END //
DELIMITER ;

# create a new user (not controller)  given Username, Password and Email
DELIMITER //
create procedure createUser(IN username TEXT,IN pass TEXT,IN email VARCHAR(255))
  begin
    INSERT INTO users values(null,username,pass,email,1);
  END //
DELIMITER ;

# Return the Username and Role given Email and Password
DELIMITER //
create procedure loginCheck(IN pass TEXT,IN email VARCHAR(255))
  begin
    select id,username,role from users where email=email and password=pass;
  END //
DELIMITER ;

# Returns all the tickets for a given User Id
DELIMITER //
create procedure getUserTickets(IN userId INT)
  begin
    select sta.uniqueId,
      sta.id,
      sta.ticketDate,
      sta.departureTime,
      stb.arrivalTime,
      sta.fromStation,
      stb.toStation,
      sta.seatNumber,
      sta.direction,
      sta.Idtrip
    from(select tickets.uniqueId,
           tickets.id,
           tickets.ticketDate,
           stations.id as idDeparture,
           steps.departureTime,
           stations.stationName as fromStation,
           seats.seatNumber,
           trips.direction,
           tickets.fkTrip as idTrip
         from tickets join stations on tickets.departureStationId=stations.id
           join steps on steps.departureStationId=stations.id
           left join bookings on tickets.id=bookings.fkTicket
           left join seats on bookings.fkSeat=seats.id
           join trips on tickets.fkTrip=trips.id
         where tickets.fkTrip=steps.fkTrip and tickets.fkUser=userId and tickets.isUsed=false)as sta
      join(select tickets.uniqueId,
             tickets.id,
             stations.id as idArrival,
             steps.arrivalTime,
             stations.stationName as toStation
           from tickets join stations on tickets.arrivalStationId=stations.id
             join steps on steps.arrivalStationId=stations.id
           where tickets.fkTrip=steps.fkTrip and tickets.fkUser=userId and tickets.isUsed=false) as stb
        on sta.id=stb.id order by id;
  END //
DELIMITER ;

# returns to the controller  all the tickets for a give Date and Trip
DELIMITER //
create procedure getTicketsControl(IN DateCheck Date,IN trip INT)
  begin
    select tickets.id,
      tickets.uniqueId,
      tickets.ticketDate,
      tickets.departureStationId,
      tickets.arrivalStationId
    from tickets where tickets.isUsed=false
                       and tickets.ticketDate=DateCheck
                       and tickets.fkTrip=trip;
  END //
DELIMITER ;

DELIMITER //
create procedure getTrips()
  begin
    select * from trips;
  END //
DELIMITER ;

DELIMITER //
create procedure buyTicket(
    IN uniqueUUID text,
    IN depStation INT,
    IN arrStation INT,
    IN ticketDate Date,
    IN price float,
    IN purchadeDate Date,
    IN userId INT,
    IN tripID INT)
  begin
    INSERT INTO tickets VALUES(NULL,uniqueUUID,
    depStation,
    arrStation,
    ticketDate,
    price,
    purchadeDate,
    userId,
    tripID,
    FALSE);
    SELECT * FROM tickets where id=last_insert_id();
  END //
DELIMITER ;

DELIMITER //
create procedure getFirstSeat(IN trip INT, in tripDate Date)
  begin
    select seatNumber,seatId,bookingId
    from (select seats.seatNumber,
            trips.skTrain,
            trips.direction,
            trains.maxCapacity,
            trains.description,
            trips.id as idTrip,
            seats.id as seatId
          from seats join trains on                                                                                                                                                                                                 seats.fkTrain=trains.id join trips on trains.id=trips.skTrain where trips.id=4) as sta left join
      (select bookings.id as bookingId,
         bookings.dateBooking,
         bookings.fkSeat,
         bookings.fkTicket,
         bookings.fkTrip
       from bookings
      where bookings.fkTrip=trip and bookings.dateBooking=tripDate) as stb
        on sta.seatId=stb.fkSeat where bookingId is null LIMIT 1;
  END //
DELIMITER ;

DELIMITER //
create procedure getAvailableSeatS(IN trip INT, in checkDate Date)
  begin
    select seatNumber,seatId,bookingId
    from (select seats.seatNumber,
            trips.skTrain,
            trips.direction,
            trains.maxCapacity,
            trains.description,
            trips.id as idTrip,
            seats.id as seatId
          from seats join trains on                                                                                                                                                                                                 seats.fkTrain=trains.id join trips on trains.id=trips.skTrain where trips.id=4) as sta left join
      (select bookings.id as bookingId,
         bookings.dateBooking,
         bookings.fkSeat,
         bookings.fkTicket,
         bookings.fkTrip
       from bookings
       where bookings.fkTrip=trip and bookings.dateBooking=checkDate) as stb
        on sta.seatId=stb.fkSeat where bookingId IS NULL;
  END //
DELIMITER ;

DELIMITER //
create procedure createBooking(IN tripDate Date, seatId INT,IN ticketID INT,IN tripId INT)
  begin
    INSERT INTO bookings VALUES(NULL,tripDate,seatId,ticketId,tripId);
  END //
DELIMITER ;

DELIMITER //
create procedure createRevisor(IN username TEXT,IN pass TEXT,IN email VARCHAR(255))
  begin
    INSERT INTO users values(null,username,pass,email,2);
  END //
DELIMITER ;

DELIMITER //
create procedure setStateToUsed(IN ticketUUID text)
  begin
    SET SQL_SAFE_UPDATES=0;
    Update tickets SET isUsed=True WHERE uniqueID=ticketUUID;
    SET SQL_SAFE_UPDATES=1;
  END //
DELIMITER ;

# Get all timetables
DELIMITER //
create procedure getTimeTable()
  begin
  select
    trains.id as idTrain,
    trains.description as traindescription,
    trips.description as tripdescription,
    trips.direction as direction,
    trips.id as tripId,
    trips.increment as increment,
    stc.idStepDep as idStep,
    stc.idStationDep as idStationDep,
    stc.idStationArr as idStationArr,
    stc.stationNameDe as stationNameDep,
    stc.stationNameArr as stationNameArr,
    stc.departureTime as departureTime,
    stc.arrivalTime as arrivalTime,
    stc.duration as duration,
    stc.waitingTime as waitingTime
  from(select * from(select steps.id as idStepDep,
	  steps.departureStationId as idStepStationDep,
    steps.fkTrip fkTripDep,
    steps.duration,
    steps.waitingTime,
    steps.departureTime,steps.arrivalTime,
    stations.id as idStationDep,
    stations.stationName as stationNameDe,
    stations.stationNumber as stationNumberDep
    from steps join stations on
	  steps.departureStationId=stations.id) as sta
  join(
  select steps.id as idStepArr,
	steps.arrivalStationId as isStepStationArr,
    steps.fkTrip as fkTripArr,
    stations.id as idStationArr,
    stations.stationName as stationNameArr,
    stations.stationNumber as stationNumberArr
    from steps join stations on
	steps.arrivalStationId=stations.id) as stb
    on sta.idStepDep=stb.IdStepArr order by idStepDep) as stc
    join trips on stc.fkTripDep=trips.id join trains on trips.skTrain=trains.id
    order by idTrain,tripId,idStep;
    end //
DELIMITER ;