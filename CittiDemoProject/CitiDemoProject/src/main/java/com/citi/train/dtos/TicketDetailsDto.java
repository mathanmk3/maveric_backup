package com.citi.train.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDetailsDto {
    private Long ticketid;
    private Long id;
    private String startDateAndTime;
    private String destinationDateAndTime;
    private String bookedDateTime;
    private String journeyDateTime;
    private String trainName;
    private Long trainNumber;
    private String boardingPoint;
    private String destinationPoint;
    @NotNull(message = "PNR Should not be Null")
    @NotBlank(message = "PNR Should not be balnk")
    private String pnrNumber;
    private String seatStatus;
    private String seatFromTo;
    private String ticketStatus;
}
