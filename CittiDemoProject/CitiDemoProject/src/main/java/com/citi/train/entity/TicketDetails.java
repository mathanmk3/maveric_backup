package com.citi.train.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "TicketDetails")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticketid")
    private Long ticketid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trianid")
    private Train trianId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private UserDetails userId;
    @Column(name = "pnrnumber")
    private String pnrNumber;
    @Column(name = "seatfromto")
    private String seatFromTo;
    @Column(name = "noofseats")
    private int noOfSeats;
    @Column(name = "boardingpoint")
    private String boardingPoint;
    @Column(name = "destinationpoint")
    private String destinationPoint;
    @Column(name = "Bookeddatetime")
    private String bookedDateTime;
    @Column(name = "ticketstatus")
    private String ticketStatus;
    @Column(name = "ticketstatusupdateddateandtime")
    private String ticketStatusUpdatedDateAndTime;
    @Column(name = "journeydatetime",columnDefinition = "DATETIME")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime journeydatetime;
    @Column(name = "seatstatus")
    private String seatStatus;



    public TicketDetails(Long ticketid,Train trianId,String destinationPoint,String boardingPoint,String seatFromTo, String pnrNumber, String bookedDateTime, String ticketStatus,LocalDateTime journeydatetime,String seatStatus) {
        this.ticketid=ticketid;
        this.trianId = trianId;
        this.pnrNumber = pnrNumber;
        this.seatFromTo = seatFromTo;
        this.boardingPoint = boardingPoint;
        this.destinationPoint = destinationPoint;
        this.bookedDateTime = bookedDateTime;
        this.ticketStatus = ticketStatus;
        this.journeydatetime=journeydatetime;
        this.seatStatus=seatStatus;
    }
}
