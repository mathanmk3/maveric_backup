package com.citi.train.response;

import com.citi.train.dtos.CancelTicketDto;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class CancelDetailResponse {
    private String message;
    private CancelTicketDto cancelTicketDto;

}
