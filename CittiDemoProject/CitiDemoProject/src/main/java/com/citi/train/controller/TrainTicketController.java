package com.citi.train.controller;

import com.citi.train.dtos.TicketDetailsDto;
import com.citi.train.exception.ServiceException;
import com.citi.train.response.BookingResponse;
import com.citi.train.service.TicketDetailService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ticket")
@CrossOrigin(value = "http://localhost:4200")
@Validated
public class TrainTicketController {
    private static final Logger logger = LoggerFactory.getLogger(TrainTicketController.class);

    @Autowired
    TicketDetailService service;
    @GetMapping("/upcoming/{pageIndex}/{pageSize}")
    public ResponseEntity<List<TicketDetailsDto>> getUpComingTickets(@PathVariable(name = "pageIndex") @NotNull int pageIndex,
                                                                     @PathVariable(name = "pageSize") @NotNull int pageSize) throws ServiceException {
        logger.info("getUpComingTickets by called|Status|");
        Long customerId = 1L;
        List<TicketDetailsDto> response = service.fetchUpComingTickets(customerId,pageIndex,pageSize);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/mybooking/{userId}/{status}/{from}/{to}/{pageIndex}/{pageSize}")
    public ResponseEntity<List<TicketDetailsDto>> getBookingHistory(@PathVariable(name = "userId") @NotNull Long customerId,
                                                                    @PathVariable(name = "status") @NotNull String status,
                                                                    @PathVariable(name = "from") @NotNull String fromDate,
                                                                    @PathVariable(name = "to") @NotNull String toDate,
                                                                    @PathVariable(name = "pageIndex") @NotNull int pageIndex,
                                                                    @PathVariable(name = "pageSize") @NotNull int pageSize)
            throws ServiceException {
        logger.info("getBookingHistory called|Status: {}|fromDate: {}|toDate: {}", status, fromDate, toDate);
        Long userId = 1L;
        List<TicketDetailsDto> response = service.myBookingTickets(userId, status, fromDate, toDate,pageIndex,pageSize);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/fetchbypnr/{userId}/{pnr}")
    public ResponseEntity<List<TicketDetailsDto>> getByPNRNumber(@PathVariable(name = "userId") @NotNull Long customerId,
                                                                 @PathVariable(name = "pnr") @NotNull @NotBlank String pnr) throws ServiceException {
        logger.info("getByPNRNumber called|PNRNumber|{}|", pnr);
        List<TicketDetailsDto> response = service.fetchTicketPNRNumber(pnr, customerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<BookingResponse> automaticTicketGenerator() {
        logger.info("AutomaticTicketGenerator controller called|");
        String responseDto = service.autogenerateTickets();
        BookingResponse response = new BookingResponse();
        response.setResponse(responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
