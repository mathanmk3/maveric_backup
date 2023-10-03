package com.citi.train.service;

import com.citi.train.dtos.TicketDetailsDto;

import java.util.List;

public interface TicketDetailService {

    public List<TicketDetailsDto> fetchUpComingTickets( Long id,int pageIndex,int pageSize);
    public List<TicketDetailsDto>  myBookingTickets(Long id,String status,String fromDate,String toDate,int pageIndex,int pageSize);
    public List<TicketDetailsDto>  fetchTicketPNRNumber(String pnrNumber,Long id);

    public String autogenerateTickets ();
}
