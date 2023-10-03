package com.citi.train.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CancelTicketDto {

    @NotNull(message = "Ticket ID  Should not be Null")
    private Long ticketid;
    @NotNull(message = "userId  Should not be Null")
    private Long userId;
    private String trainName;
    private Long trainNumber;
    @NotNull(message = "Resson Should not be Null")
    @NotBlank(message = "Resson Should not be balnk")
    private String cancelReason;
    private String refundStatus;
    private LocalDateTime cancelDateTime;
    private String bookedDateTime;
    private String journeyDateTime;
    private String pnrNumber;

}
