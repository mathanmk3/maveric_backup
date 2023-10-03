package com.citi.train.serviceimpl;

import com.citi.train.dtos.TicketDetailsDto;
import com.citi.train.entity.TicketDetails;
import com.citi.train.entity.Train;
import com.citi.train.entity.UserDetails;
import com.citi.train.enums.TicketStatus;
import com.citi.train.exception.ErrorCodes;
import com.citi.train.exception.SQLExceptions;
import com.citi.train.exception.ServiceException;
import com.citi.train.repository.TicketDetailsRepo;
import com.citi.train.repository.TrainRepo;
import com.citi.train.repository.UserRepo;
import com.citi.train.service.TicketDetailService;
import com.citi.train.uitils.DateUtils;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class TicketDetailsServiceImpl implements TicketDetailService {

    @Autowired
    TicketDetailsRepo repo;
    @Autowired
    TrainRepo trainRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(TicketDetailsServiceImpl.class);

    /**
     *
     * @param userid
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws ServiceException
     * @throws SQLExceptions
     * @apiNote Api is used to fetch all Upcoming Ticket with given date range and having status YETTOSTART
     */
    @Override
    public List<TicketDetailsDto> fetchUpComingTickets(Long userid,int pageIndex,int pageSize) throws ServiceException, SQLExceptions {
        try {
            logger.info("fetchUpComingTickets Service Called|");
            String status = String.valueOf(TicketStatus.YETTOSTART);
            LocalDateTime currentDateTime = DateUtils.currentDateTimeFormatInDateFormat();
            logger.info("fetchUpComingTickets Service Called|{}|{}", status, currentDateTime);
            Optional<List<TicketDetails>> ticketEntity = repo.getUpComingTickets(status, userid, currentDateTime, PageRequest.of(pageIndex, pageSize));
            List<TicketDetailsDto> responseDto = new ArrayList<>();
            if (ticketEntity.isPresent() && !ticketEntity.get().isEmpty()) {
                for (TicketDetails ticket : ticketEntity.get()) {
                    TicketDetailsDto response = modelMapper.map(ticket, TicketDetailsDto.class);
                    modelMapper.map(ticket.getTrianId(), response);
                    responseDto.add(response);
                }
                return responseDto;
            } else {
                logger.error("FetchUpComingTickets|{}|{}", status, ErrorCodes.NO_UPCOMING_TICKETS);
                throw new ServiceException(ErrorCodes.NO_UPCOMING_TICKETS);
            }
        } catch (DataAccessException | MappingException he) {
            logger.error("ErroMessages", he.fillInStackTrace());
            throw new SQLExceptions(ErrorCodes.CONNECTION_ISSUE);
        }
    }

    /**
     *
     * @param userid
     * @param status
     * @param fromDate
     * @param toDate
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws ServiceException
     * @throws SQLExceptions
     * @apiNote This API fetch all booking details of the user between the given date range and status
     */
    @Override
    public List<TicketDetailsDto> myBookingTickets(Long userid, String status, String fromDate, String toDate,int pageIndex,int pageSize) throws ServiceException, SQLExceptions {
        try {
            logger.info("MyBookingTickets Service Called");
            LocalDateTime from = DateUtils.stringToDateFormat(fromDate);
            LocalDateTime to = DateUtils.stringToDateFormat(toDate);
            logger.info("MyBookingTickets Service Called|{}|{}|{}", status, from, to);
            Optional<List<TicketDetails>> ticketEntity = repo.findByUserId_Id(userid, status, from, to,PageRequest.of(pageIndex, pageSize));
            List<TicketDetailsDto> responseDto = new ArrayList<>();
            if (ticketEntity.isPresent() && !ticketEntity.get().isEmpty()) {
                for (TicketDetails ticket : ticketEntity.get()) {
                    TicketDetailsDto response = modelMapper.map(ticket, TicketDetailsDto.class);
                    modelMapper.map(ticket.getTrianId(), response);
                    responseDto.add(response);
                }
            } else {
                logger.error("MyBookingTickets|{}|{}", status, ErrorCodes.NO_BOOKING_FOUND);
                throw new ServiceException(ErrorCodes.NO_BOOKING_FOUND);
            }
            return responseDto;
        } catch (DataAccessException | MappingException he) {
            logger.error("ErroMessage-MyBookingTickets", he.fillInStackTrace());
            throw new SQLExceptions(ErrorCodes.CONNECTION_ISSUE);
        }
    }

    /**
     *
     * @param pnrNumber
     * @param id
     * @return
     * @throws ServiceException
     * @throws SQLExceptions
     * @apiNote This API is used to fetch the tickets based on the PNR Number
     */
    @Override
    public List<TicketDetailsDto> fetchTicketPNRNumber(String pnrNumber, Long id) throws ServiceException, SQLExceptions {
        try {
            logger.info("FetchTicketPNRNumber Service Called|PNRNumber {}" , pnrNumber);
            Optional<List<TicketDetails>> ticketEntity = repo.getByPNRNumber(pnrNumber, id);
            List<TicketDetailsDto> responseDto = new ArrayList<>();
            if (ticketEntity.isPresent() && !ticketEntity.get().isEmpty()) {
                for (TicketDetails ticket : ticketEntity.get()) {
                    TicketDetailsDto response = modelMapper.map(ticket, TicketDetailsDto.class);
                    modelMapper.map(ticket.getTrianId(), response);
                    responseDto.add(response);
                }
            } else {
                logger.error("FetchTicketPNRNumber|{}|{}", pnrNumber, ErrorCodes.NO_TICKET_PNR);
                throw new ServiceException(ErrorCodes.NO_TICKET_PNR);
            }
            return responseDto;
        } catch (DataAccessException | MappingException he) {
            logger.error("ErroMessage-FetchTicketPNRNumber", he.fillInStackTrace());
            throw new SQLExceptions(ErrorCodes.CONNECTION_ISSUE);
        }

    }

    /**
     * @apiNote this API is used to generate the ticket Automatically for viewing purpose
     * @return
     */
    @Override
    public String autogenerateTickets() {
        try {
            Optional<Train> trainDetails = trainRepo.findByTrainNumber(16504L);
            Optional<UserDetails> userDetails = userRepo.findById(1L);
            Random random = new Random();
            if (trainDetails.isPresent() && userDetails.isPresent()) {
                String pnrNumber = "PNR" + random.nextInt(10000);
                TicketDetails ticketDetails = TicketDetails.builder()
                        .trianId(trainDetails.get()).boardingPoint("Chennai")
                        .destinationPoint("Kovilpatti").noOfSeats(2)
                        .seatFromTo("U12,L10").bookedDateTime(DateUtils.currentDateTimeFormatInString())
                        .ticketStatus("YETTOSTART").seatStatus("CNF")
                        .ticketStatusUpdatedDateAndTime(DateUtils.currentDateTimeFormatInString())
                        .journeydatetime(DateUtils.generateSpecificDate(5L)).pnrNumber(pnrNumber)
                        .userId(userDetails.get())
                        .build();
                repo.save(ticketDetails);
                Optional<Train> trainDetails1 = trainRepo.findByTrainNumber(16506L);
                Optional<UserDetails> userDetails1 = userRepo.findById(1L);
                if (trainDetails1.isPresent() && userDetails1.isPresent() ) {
                    String pnrNumber1 = "PNR" + random.nextInt(10000);
                    TicketDetails ticketDetails1 = TicketDetails.builder()
                            .trianId(trainDetails1.get()).boardingPoint("Chennai")
                            .destinationPoint("Tuticorin").noOfSeats(2)
                            .seatFromTo("U12,L10,M11,SL15").bookedDateTime(DateUtils.currentDateTimeFormatInString())
                            .ticketStatus("COMPLETED").seatStatus("WL")
                            .ticketStatusUpdatedDateAndTime(DateUtils.currentDateTimeFormatInString())
                            .journeydatetime(DateUtils.generateSpecificDate(3L)).pnrNumber(pnrNumber1)
                            .userId(userDetails1.get())
                            .build();
                    repo.save(ticketDetails1);

                }
            }
            return "AumtoMatic ticket generated";
        } catch (DataAccessException | MappingException he) {
            logger.error("ErroMessage", he.fillInStackTrace());
            throw new SQLExceptions(ErrorCodes.CONNECTION_ISSUE);
        }
    }
}
