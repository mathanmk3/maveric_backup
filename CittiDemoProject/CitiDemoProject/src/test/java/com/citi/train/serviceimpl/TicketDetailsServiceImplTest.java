package com.citi.train.serviceimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.citi.train.dtos.CancelTicketDto;
import com.citi.train.dtos.TicketDetailsDto;
import com.citi.train.entity.TicketDetails;
import com.citi.train.entity.Train;
import com.citi.train.entity.UserDetails;
import com.citi.train.exception.ErrorCodes;
import com.citi.train.exception.SQLExceptions;
import com.citi.train.exception.ServiceException;
import com.citi.train.repository.TicketDetailsRepo;
import com.citi.train.repository.TrainRepo;
import com.citi.train.repository.UserRepo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {TicketDetailsServiceImpl.class})
@ExtendWith(SpringExtension.class)
class TicketDetailsServiceImplTest {
    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private TicketDetailsRepo ticketDetailsRepo;

    @Autowired
    private TicketDetailsServiceImpl ticketDetailsServiceImpl;

    @MockBean
    private TrainRepo trainRepo;

    @MockBean
    private UserRepo userRepo;
    Train trianId = new Train();
    UserDetails userId = new UserDetails();
    TicketDetails ticketDetails = new TicketDetails();

    @BeforeEach
    void setup() {

        trianId.setDestinationDateAndTime("2020-03-01");
        trianId.setEndPoint("KovilPatti");
        trianId.setStartDateAndTime("2023-03-03");
        trianId.setStartPoint("Chennai");
        trianId.setTainName("Nellai Express");
        trianId.setTrainId(1L);
        trianId.setTrainNumber(1L);

        userId.setId(1L);
        userId.setMailId("matha33@gmail.com");
        userId.setPassword("mathan33");
        userId.setStatus("1");
        userId.setUserName("mathanMk");

        ticketDetails.setBoardingPoint("Chennai");
        ticketDetails.setBookedDateTime("2023-03-01");
        ticketDetails.setDestinationPoint("Kovilpatti");
        ticketDetails.setJourneydatetime(LocalDate.of(2023, 4, 1).atStartOfDay());
        ticketDetails.setNoOfSeats(2);
        ticketDetails.setPnrNumber("PNR4231");
        ticketDetails.setSeatFromTo("U1,M2");
        ticketDetails.setSeatStatus("CNF");
        ticketDetails.setTicketStatus("YETTOSTART");
        ticketDetails.setTicketStatusUpdatedDateAndTime("2023-03-03");
        ticketDetails.setTicketid(1L);
        ticketDetails.setTrianId(trianId);
        ticketDetails.setUserId(userId);

        TicketDetailsDto ticketDetailsDto = TicketDetailsDto.builder()
                .id(1L)
                .ticketStatus("COMPLETED")
                .boardingPoint("Chennai")
                .destinationPoint("Madurai")
                .journeyDateTime("2020-03-01")
                .bookedDateTime("2020-03-01")
                .destinationDateAndTime("2020-03-01")
                .pnrNumber("PNR123434")
                .startDateAndTime("2020-03-01")
                .trainName("Nellai")
                .seatFromTo("2to5")
                .seatStatus("CF")
                .ticketid(1L)
                .trainNumber(16469L)
                .build();

        ticketDetailsDto.getId();
        ticketDetailsDto.getTicketStatus();
        ticketDetailsDto.getBoardingPoint();
        ticketDetailsDto.getBookedDateTime();
        ticketDetailsDto.getDestinationDateAndTime();
        ticketDetailsDto.getTicketid();
        ticketDetailsDto.getPnrNumber();
        ticketDetailsDto.getTrainNumber();
        ticketDetailsDto.getSeatStatus();
        ticketDetailsDto.getSeatFromTo();
        ticketDetailsDto.getStartDateAndTime();
        ticketDetailsDto.getJourneyDateTime();
        ticketDetailsDto.getTicketid();
        ticketDetailsDto.getTrainName();
        ticketDetailsDto.getDestinationPoint();
        ticketDetailsDto.toString();

    }

    @Test
    void testFetchUpComingTickets() throws SQLExceptions, ServiceException {
        TicketDetailsDto tckDto = mock(TicketDetailsDto.class);

        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<TicketDetailsDto>>any()))
                .thenReturn(new TicketDetailsDto());
        doNothing().when(modelMapper).map(Mockito.<Object>any(), Mockito.<Object>any());
        ArrayList<TicketDetails> ticketDetailsList = new ArrayList<>();
        ticketDetailsList.add(ticketDetails);
        Optional<List<TicketDetails>> ofResult = Optional.of(ticketDetailsList);
        when(ticketDetailsRepo.getUpComingTickets(Mockito.<String>any(), Mockito.<Long>any(),
                Mockito.<LocalDateTime>any(), Mockito.<Pageable>any())).thenReturn(ofResult);
        assertEquals(1, ticketDetailsServiceImpl.fetchUpComingTickets(1L, 1, 3).size());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<TicketDetailsDto>>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Object>any());
        verify(ticketDetailsRepo).getUpComingTickets(Mockito.<String>any(), Mockito.<Long>any(),
                Mockito.<LocalDateTime>any(), Mockito.<Pageable>any());
    }

    @Test
    void testFetchUpComingTicketsWhenNoTicketsFound() throws SQLExceptions, ServiceException {
        when(ticketDetailsRepo.getUpComingTickets(Mockito.<String>any(), Mockito.<Long>any(),
                Mockito.<LocalDateTime>any(), Mockito.<Pageable>any())).thenReturn(Optional.empty());
        try {
            ticketDetailsServiceImpl.fetchUpComingTickets(1L, 1, 3);
        } catch (ServiceException e) {
            assertEquals(ErrorCodes.NO_UPCOMING_TICKETS, e.getMessage());
            verify(ticketDetailsRepo).getUpComingTickets(Mockito.<String>any(), Mockito.<Long>any(),
                    Mockito.<LocalDateTime>any(), Mockito.<Pageable>any());
        }
    }

    @Test
    void testFetchUpComingTicketsWhenNoConnection() throws SQLExceptions, ServiceException {
        when(ticketDetailsRepo.getUpComingTickets(Mockito.<String>any(), Mockito.<Long>any(),
                Mockito.<LocalDateTime>any(), Mockito.<Pageable>any())).thenThrow(new DataAccessException("Database error") {
        });
        try {
            ticketDetailsServiceImpl.fetchUpComingTickets(1L, 1, 3);
        } catch (DataAccessException e) {
            assertEquals(ErrorCodes.CONNECTION_ISSUE, e.getMessage());
            verify(ticketDetailsRepo).getUpComingTickets(Mockito.<String>any(), Mockito.<Long>any(),
                    Mockito.<LocalDateTime>any(), Mockito.<Pageable>any());      }
    }

    @Test
    void testMyBookingTickets() throws SQLExceptions, ServiceException {
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<TicketDetailsDto>>any()))
                .thenReturn(new TicketDetailsDto());
        doNothing().when(modelMapper).map(Mockito.<Object>any(), Mockito.<Object>any());
        ArrayList<TicketDetails> ticketDetailsList = new ArrayList<>();
        ticketDetailsList.add(ticketDetails);
        Optional<List<TicketDetails>> ofResult = Optional.of(ticketDetailsList);
        when(ticketDetailsRepo.findByUserId_Id(Mockito.<Long>any(), Mockito.<String>any(),
                Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any(), Mockito.<Pageable>any())).thenReturn(ofResult);
        assertEquals(1, ticketDetailsServiceImpl.myBookingTickets(1L, "Completed", "2022-09-08 12:28:22", "2024-09-08 12:28:22", 1, 3).size());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<TicketDetailsDto>>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Object>any());
        verify(ticketDetailsRepo).findByUserId_Id(Mockito.<Long>any(), Mockito.<String>any(),
                Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any(), Mockito.<Pageable>any());
    }

    @Test
    void ttestMyBookingTicketsWhenNoTicketsFound() throws SQLExceptions, ServiceException {
        when(ticketDetailsRepo.findByUserId_Id(Mockito.<Long>any(), Mockito.<String>any(),
                Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any(), Mockito.<Pageable>any())).thenReturn(Optional.empty());
        try {
            ticketDetailsServiceImpl.myBookingTickets(1L, "Completed", "2022-09-08 12:28:22", "2024-09-08 12:28:22", 1, 3).size() ;
        } catch (ServiceException e) {
            assertEquals(ErrorCodes.NO_BOOKING_FOUND, e.getMessage());
            verify(ticketDetailsRepo).findByUserId_Id(Mockito.<Long>any(), Mockito.<String>any(),
                    Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any(), Mockito.<Pageable>any());
        }
    }

    @Test
    void ttestMyBookingTicketsWhenConnection() throws SQLExceptions, ServiceException {
        when(ticketDetailsRepo.findByUserId_Id(Mockito.<Long>any(), Mockito.<String>any(),
                Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any(), Mockito.<Pageable>any())).thenThrow(new DataAccessException("Database error") {
        });
        try {
            ticketDetailsServiceImpl.myBookingTickets(1L, "Completed", "2022-09-08 12:28:22", "2024-09-08 12:28:22", 1, 3).size() ;
        } catch (DataAccessException e) {
            assertEquals(ErrorCodes.CONNECTION_ISSUE, e.getMessage());
            verify(ticketDetailsRepo).findByUserId_Id(Mockito.<Long>any(), Mockito.<String>any(),
                    Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any(), Mockito.<Pageable>any());        }
    }

    @Test
    void testFetchTicketPNRNumber() throws SQLExceptions, ServiceException {
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<TicketDetailsDto>>any()))
                .thenReturn(new TicketDetailsDto());
        doNothing().when(modelMapper).map(Mockito.<Object>any(), Mockito.<Object>any());
        ArrayList<TicketDetails> ticketDetailsList = new ArrayList<>();
        ticketDetailsList.add(ticketDetails);
        Optional<List<TicketDetails>> ofResult = Optional.of(ticketDetailsList);
        when(ticketDetailsRepo.getByPNRNumber(Mockito.<String>any(), Mockito.<Long>any())).thenReturn(ofResult);
        assertEquals(1, ticketDetailsServiceImpl.fetchTicketPNRNumber("42", 1L).size());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<TicketDetailsDto>>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Object>any());
        verify(ticketDetailsRepo).getByPNRNumber(Mockito.<String>any(), Mockito.<Long>any());
    }

    @Test
    void testFetchTicketPNRNumberWhenNoTicketsFound() throws SQLExceptions, ServiceException {
        when(ticketDetailsRepo.getByPNRNumber(Mockito.<String>any(), Mockito.<Long>any())).thenReturn(Optional.empty());
        try {
            ticketDetailsServiceImpl.fetchTicketPNRNumber("PNR42", 1L).size();
        } catch (ServiceException e) {
            assertEquals(ErrorCodes.NO_TICKET_PNR, e.getMessage());
            verify(ticketDetailsRepo).getByPNRNumber(Mockito.<String>any(), Mockito.<Long>any());

        }
    }
    @Test
    void testFetchTicketPNRNumberConnection() throws SQLExceptions, ServiceException {
        when(ticketDetailsRepo.getByPNRNumber(Mockito.<String>any(), Mockito.<Long>any())).thenThrow(new DataAccessException("Database error") {
        });
        try {
            ticketDetailsServiceImpl.fetchTicketPNRNumber("PNR42", 1L).size();
        } catch (DataAccessException e) {
            assertEquals(ErrorCodes.CONNECTION_ISSUE, e.getMessage());
            verify(ticketDetailsRepo).getByPNRNumber(Mockito.<String>any(), Mockito.<Long>any());
        }
    }

    @Test
    void testAutogenerateTickets() {
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<TicketDetailsDto>>any()))
                .thenReturn(new TicketDetailsDto());
        when(ticketDetailsRepo.save(Mockito.<TicketDetails>any())).thenReturn(ticketDetails);

        Train train = new Train();
        train.setDestinationDateAndTime("2020-03-01");
        train.setEndPoint("KovilPatti");
        train.setStartDateAndTime("2023-03-03");
        train.setStartPoint("Chennai");
        train.setTainName("Nellai Express");
        train.setTrainId(1L);
        train.setTrainNumber(1L);

        Optional<Train> ofResult = Optional.of(train);
        when(trainRepo.findByTrainNumber(Mockito.<Long>any())).thenReturn(ofResult);
        UserDetails userDetails = new UserDetails();
        userDetails.setId(1L);
        userDetails.setMailId("matha33@gmail.com");
        userDetails.setPassword("mathan33");
        userDetails.setStatus("1");
        userDetails.setUserName("mathanMk");

        Optional<UserDetails> ofResult2 = Optional.of(userDetails);
        when(userRepo.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        assertEquals("AumtoMatic ticket generated", ticketDetailsServiceImpl.autogenerateTickets());
        verify(ticketDetailsRepo, atLeast(1)).save(Mockito.<TicketDetails>any());
        verify(trainRepo, atLeast(1)).findByTrainNumber(Mockito.<Long>any());
        verify(userRepo, atLeast(1)).findById(Mockito.<Long>any());
    }

    @Test
    void testAutogenerateTicketsTicketsFound() throws SQLExceptions, ServiceException {

        when(trainRepo.findByTrainNumber(Mockito.<Long>any())).thenReturn(Optional.empty());
        try {
            ticketDetailsServiceImpl.autogenerateTickets();
        } catch (ServiceException e) {
            assertEquals(ErrorCodes.NO_TRAIN_FOUND, e.getMessage());
            verify(ticketDetailsRepo, atLeast(1)).save(Mockito.<TicketDetails>any());
            verify(trainRepo, atLeast(1)).findByTrainNumber(Mockito.<Long>any());
        }
    }

    @Test
    void testAutogenerateWhenNoConnection() throws SQLExceptions, ServiceException {

        when(trainRepo.findByTrainNumber(Mockito.<Long>any())).thenThrow(new DataAccessException("Database error") {
        });
        try {
            ticketDetailsServiceImpl.autogenerateTickets();
        }catch (DataAccessException e) {
            assertEquals(ErrorCodes.CONNECTION_ISSUE, e.getMessage());
            verify(trainRepo, atLeast(1)).findByTrainNumber(Mockito.<Long>any());
        }
    }


}

