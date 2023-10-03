package com.citi.train.controller;

import static org.mockito.Mockito.when;

import com.citi.train.dtos.CancelTicketDto;
import com.citi.train.response.CancelDetailResponse;
import com.citi.train.service.CancelDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {CancelTicketController.class})
@ExtendWith(SpringExtension.class)
class CancelTicketControllerTest {
    @MockBean
    private CancelDetailResponse cancelDetailResponse;
    @MockBean
    private CancelDetailService cancelDetailService;
    @Autowired
    private CancelTicketController cancelTicketController;

    @Test
    void testCancelTicket() throws Exception {

        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/cancelticket")
                .contentType(MediaType.APPLICATION_JSON);

        CancelTicketDto cancelTicketDto = new CancelTicketDto();
        cancelTicketDto.setBookedDateTime("2023-03-03");
        cancelTicketDto.setCancelReason("Wrong Date");
        cancelTicketDto.setJourneyDateTime("2023-03-03");
        cancelTicketDto.setPnrNumber("PNR427");
        cancelTicketDto.setRefundStatus("INPROGRESS");
        cancelTicketDto.setTrainName("Nellai Express");
        cancelTicketDto.setTicketid(1L);
        cancelTicketDto.setTrainNumber(16504L);
        cancelTicketDto.setUserId(1L);
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content((new ObjectMapper()).writeValueAsString(cancelTicketDto));
        MockMvcBuilders.standaloneSetup(cancelTicketController).build().perform(requestBuilder);
    }

    @Test
    void testGetAllCancelTickets() throws Exception {
        when(cancelDetailService.fetchAllCancelTicket(Mockito.<Long>any(), Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/cancelticket/{from}/{to}",
                "2023-09-10 13:09:31.000", "2023-09-14 13:09:31.000");
        MockMvcBuilders.standaloneSetup(cancelTicketController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testGetAllCancelTickets2cases() throws Exception {
        when(cancelDetailService.fetchAllCancelTicket(Mockito.<Long>any(), Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/cancelticket/{from}/{to}",
                "2023-09-10 13:09:31.000", "2023-09-10 13:09:31.000");
        MockMvcBuilders.standaloneSetup(cancelTicketController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}

