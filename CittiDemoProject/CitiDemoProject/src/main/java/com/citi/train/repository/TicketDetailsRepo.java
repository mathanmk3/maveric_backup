package com.citi.train.repository;

import com.citi.train.entity.TicketDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketDetailsRepo extends JpaRepository<TicketDetails,Long> {


    @Query(value = "SELECT new com.citi.train.entity.TicketDetails(td.ticketid,td.trianId,td.destinationPoint,td.boardingPoint,td.seatFromTo,td.pnrNumber,td.bookedDateTime,td.ticketStatus,td.journeydatetime,td.seatStatus) FROM TicketDetails td" +
            " INNER JOIN Train t ON t.trainId =td.trianId.trainId " +
            " INNER JOIN UserDetails ud ON ud.Id= td.userId.Id " +
            " WHERE td.pnrNumber=:pnr AND ud.Id=:id")
    Optional<List<TicketDetails>> getByPNRNumber(@Param("pnr") String pnr, @Param("id") Long id);

    @Query(value = "SELECT new com.citi.train.entity.TicketDetails(td.ticketid AS ticketId,td.trianId,td.destinationPoint,td.boardingPoint,td.seatFromTo,td.pnrNumber,td.bookedDateTime,td.ticketStatus,td.journeydatetime,td.seatStatus) FROM TicketDetails td" +
            " INNER JOIN Train t ON t.trainId =td.trianId.trainId " +
            " INNER JOIN UserDetails ud ON ud.Id= td.userId.Id " +
            " WHERE td.ticketStatus=:ticketStatus AND ud.Id=:id AND td.journeydatetime>=:journeydatetime")
    Optional<List<TicketDetails>> getUpComingTickets(@Param("ticketStatus") String pnr, @Param("id") Long id, @Param("journeydatetime") LocalDateTime journeydatetime, Pageable pageable);

    @Query(value = "SELECT new com.citi.train.entity.TicketDetails(td.ticketid,td.trianId,td.destinationPoint,td.boardingPoint,td.seatFromTo,td.pnrNumber,td.bookedDateTime,td.ticketStatus,td.journeydatetime,td.seatStatus) FROM TicketDetails td" +
            " INNER JOIN Train t ON t.trainId =td.trianId.trainId " +
            " INNER JOIN UserDetails ud ON ud.Id= td.userId.Id " +
            " WHERE ud.Id=:id AND td.ticketStatus=:ticketStatus AND td.journeydatetime>=:journeydatetime AND td.journeydatetime<=:journeydatetime1")
    Optional<List<TicketDetails>> findByUserId_Id(@Param("id") Long id,@Param("ticketStatus") String ticketStatus,
                                                  @Param("journeydatetime") LocalDateTime journeydatetime,
                                                  @Param("journeydatetime1") LocalDateTime journeydatetime1,
                                                  Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE TicketDetails SET ticketStatus=:ticketStatus,ticketStatusUpdatedDateAndTime=:ticketStatusUpdatedDateAndTime WHERE ticketid=:ticketid")
    int ticketStatus(@Param("ticketStatus") String ticketStatus, @Param("ticketStatusUpdatedDateAndTime") String ticketStatusUpdatedDateAndTime,
                                       @Param("ticketid") Long id);

}
