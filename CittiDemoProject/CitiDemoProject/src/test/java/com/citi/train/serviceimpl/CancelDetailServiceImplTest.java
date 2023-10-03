package com.citi.train.serviceimpl;

import com.citi.train.dtos.CancelTicketDto;
import com.citi.train.dtos.TicketDetailsDto;
import com.citi.train.entity.CanceledTicket;
import com.citi.train.entity.TicketDetails;
import com.citi.train.entity.Train;
import com.citi.train.entity.UserDetails;
import com.citi.train.exception.ErrorCodes;
import com.citi.train.exception.SQLExceptions;
import com.citi.train.exception.ServiceException;
import com.citi.train.repository.CancelTicketRepo;
import com.citi.train.repository.TicketDetailsRepo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.citi.train.uitils.CommonUtils;
import com.citi.train.uitils.DateUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ContextConfiguration(classes = {CancelDetailServiceImpl.class})
@ExtendWith(SpringExtension.class)
class CancelDetailServiceImplTest {
    @Autowired
    private CancelDetailServiceImpl cancelDetailServiceImpl;

    @MockBean
    private CancelTicketRepo cancelTicketRepo;

    @MockBean
    private ModelMapper modelMapper;

    static MockedStatic<CommonUtils> utilities;


    @MockBean
    private TicketDetailsRepo ticketDetailsRepo;

    CanceledTicket canceledTicket = new CanceledTicket();
    UserDetails userId = new UserDetails();

    TicketDetails ticketDetails = new TicketDetails();

    Train trianId = new Train();

    @BeforeAll
    static void setupAll() {
        utilities = Mockito.mockStatic(CommonUtils.class);
    }


    @BeforeEach
    void setup(){

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

        canceledTicket.setCanceledId(1L);
        canceledTicket.setRefundStatus("Refund details");
        canceledTicket.setCancelDateTime(DateUtils.currentDateTimeFormatInDateFormat());
        canceledTicket.setRefundStatus("INPROGRESS");
        canceledTicket.setTicketId(ticketDetails);
        canceledTicket.setUserId(userId);
    }

    @Test
    void testFetchAllCancelTicket() throws ServiceException {

        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<CancelTicketDto>>any()))
                .thenReturn(new CancelTicketDto());
        doNothing().when(modelMapper).map(Mockito.<Object>any(), Mockito.<Object>any());
        ArrayList<CanceledTicket> ticketDetailsList = new ArrayList<>();
        ticketDetailsList.add(canceledTicket);
        Optional<List<CanceledTicket>> ofResult = Optional.of(ticketDetailsList);
        System.out.println(cancelTicketRepo);
        when(cancelTicketRepo.getCancelTickets(Mockito.<Long>any(),Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any()
              )).thenReturn(ofResult);
        assertEquals(1, cancelDetailServiceImpl.fetchAllCancelTicket(1L, "2022-09-08 12:28:22", "2024-09-08 12:28:22").size());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<CancelTicketDto>>any());
        verify(cancelTicketRepo).getCancelTickets(Mockito.<Long>any(),Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any());
    }

    @Test
    void testFetchAllCancelTicketWhenNoTicketsFound() throws SQLExceptions, ServiceException {
        when(cancelTicketRepo.getCancelTickets(Mockito.<Long>any(),Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any()
        )).thenReturn(Optional.empty());
        try {
            cancelDetailServiceImpl.fetchAllCancelTicket(1L, "2022-09-08 12:28:22", "2024-09-08 12:28:22");
        } catch (ServiceException e) {
            assertEquals(ErrorCodes.NO_CANCELED_TICKET, e.getMessage());
            verify(cancelTicketRepo).getCancelTickets(Mockito.<Long>any(),Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any());

        }
    }
    @Test
    void testFetchAllCancelTicketWhenNoConnection() throws SQLExceptions, ServiceException {
        when(cancelTicketRepo.getCancelTickets(Mockito.<Long>any(),Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any()
        )).thenThrow(new DataAccessException("Database error") {
        });
        try {
            cancelDetailServiceImpl.fetchAllCancelTicket(1L, "2022-09-08 12:28:22", "2024-09-08 12:28:22");
        } catch (DataAccessException e) {
            assertEquals(ErrorCodes.CONNECTION_ISSUE, e.getMessage());
            verify(cancelTicketRepo).getCancelTickets(Mockito.<Long>any(),Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any());
        }
    }

    @Test
    void testCancelTicket() throws SQLExceptions, ServiceException {
        CanceledTicket canceledTicketsaved = mock(CanceledTicket.class);
        CancelTicketDto canceledDto = mock(CancelTicketDto.class);
        utilities.when(() -> CommonUtils.getMapper(canceledDto, CanceledTicket.class)).thenReturn(canceledTicketsaved);
        when(cancelTicketRepo.save(Mockito.any(CanceledTicket.class))).thenReturn(canceledTicketsaved);
        utilities.when(() -> CommonUtils.getMapper(canceledTicketsaved, CancelTicketDto.class)).thenReturn(canceledDto);
        CancelTicketDto expected = cancelDetailServiceImpl.cancelTicket(canceledDto);
        assertNotNull(expected);
        verify(cancelTicketRepo).save(Mockito.any(CanceledTicket.class));
    }

    @Test
    void testCancelTicketWhenNoConnection() throws SQLExceptions, ServiceException {
        CanceledTicket canceledTicketsaved = mock(CanceledTicket.class);
        CancelTicketDto canceledDto = mock(CancelTicketDto.class);
        utilities.when(() -> CommonUtils.getMapper(canceledDto, CanceledTicket.class)).thenReturn(canceledTicketsaved);
        when(cancelTicketRepo.save(Mockito.any(CanceledTicket.class))).thenThrow(new DataAccessException("Database error") {
        });
        try {
            CancelTicketDto expected = cancelDetailServiceImpl.cancelTicket(canceledDto);
        } catch (DataAccessException e) {
            assertEquals(ErrorCodes.CONNECTION_ISSUE, e.getMessage());
            verify(cancelTicketRepo).save(Mockito.any(CanceledTicket.class));
        }
    }
}

