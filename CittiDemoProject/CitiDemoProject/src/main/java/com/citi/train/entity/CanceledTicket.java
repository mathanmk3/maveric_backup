package com.citi.train.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CanceledTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "canceledid")
    private Long canceledId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticketid")
    private TicketDetails ticketId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private UserDetails userId;
    @Column(name = "cancelreason")
    private String cancelReason;
    @Column(name = "refundstatus")
    private String refundStatus;
    @Column(name = "canceldatetime")
    private LocalDateTime cancelDateTime;

    public CanceledTicket(TicketDetails ticketId, String cancelReason, String refundStatus, LocalDateTime cancelDateTime) {
        this.ticketId=ticketId;
        this.cancelReason = cancelReason;
        this.refundStatus = refundStatus;
        this.cancelDateTime = cancelDateTime;
    }
}
