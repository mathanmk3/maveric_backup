package com.citi.train.controller;


import com.citi.train.dtos.CancelTicketDto;
import com.citi.train.exception.ServiceException;
import com.citi.train.response.CancelDetailResponse;
import com.citi.train.service.CancelDetailService;
import com.citi.train.uitils.CommonUtils;
import jakarta.validation.Valid;

import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cancelticket")
@CrossOrigin(origins = "http://localhost:4200",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = "*")
public class CancelTicketController {
    private static final Logger logger = LoggerFactory.getLogger(CancelTicketController.class);

    @Autowired
    CancelDetailService service;
    @Autowired
    CancelDetailResponse response;
    @PostMapping
    public ResponseEntity<CancelDetailResponse> cancelTicket(@RequestBody @Valid CancelTicketDto dto) throws ServiceException{
        logger.info("cancelTicket Controller calss called|");
        CancelTicketDto cancelSetails = service.canc    elTicket(dto);
        if(!CommonUtils.checkNullable(cancelSetails)){
            response.setMessage("Ticket Canceled");
            response.setCancelTicketDto(cancelSetails);
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @GetMapping("/{from}/{to}")
    public ResponseEntity<List<CancelTicketDto>> getAllCancelTickets(
            @PathVariable(name = "from") @NotNull String fromDate,
            @PathVariable(name = "to") @NotNull String toDate)throws ServiceException {

        logger.info("getAllCancelTickets Controller class called|fromDate {}|toDate {}", fromDate, toDate);
        Long customerId=1L;
        List<CancelTicketDto> responseDto = service.fetchAllCancelTicket(customerId,fromDate,toDate);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
