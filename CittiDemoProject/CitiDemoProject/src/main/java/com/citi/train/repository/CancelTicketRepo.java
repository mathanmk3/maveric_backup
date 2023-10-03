package com.citi.train.repository;

import com.citi.train.entity.CanceledTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface CancelTicketRepo extends JpaRepository<CanceledTicket,Long> {


    @Query(value = "SELECT new com.citi.train.entity.CanceledTicket(ct.ticketId,cancelReason,refundStatus,cancelDateTime) FROM CanceledTicket ct" +
            " INNER JOIN TicketDetails td ON td.ticketid =ct.ticketId.ticketid " +
            " INNER JOIN UserDetails ud ON ud.Id= ct.userId.Id " +
            " WHERE ct.cancelDateTime>=:cancelDateTime AND ct.cancelDateTime<=:cancelDateTime1 AND ud.Id=:id")
    Optional<List<CanceledTicket>> getCancelTickets( @Param("id") Long id,@Param("cancelDateTime") LocalDateTime cancelDateTime,@Param("cancelDateTime1") LocalDateTime cancelDateTime1);


}
