package com.citi.train.service;

import com.citi.train.dtos.CancelTicketDto;


import java.util.List;

public interface CancelDetailService {

    public List<CancelTicketDto> fetchAllCancelTicket(Long userId,String fromDate,String toDate);

    public CancelTicketDto cancelTicket(CancelTicketDto dto);
}
