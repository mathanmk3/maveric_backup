package com.citi.train.response;

import com.citi.train.entity.TicketDetails;
import lombok.Data;

@Data
public class BookingResponse {

    private String response;

    private TicketDetails data;



}
