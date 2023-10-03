package com.citi.train.controller;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

import com.citi.train.service.TicketDetailService;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {TrainTicketController.class})
@ExtendWith(SpringExtension.class)
class TrainTicketControllerTest {
    @MockBean
    private TicketDetailService ticketDetailService;
    @Autowired
    private TrainTicketController trainTicketController;

    @Test
    void testAutomaticTicketGenerator() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/ticket");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(trainTicketController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.
                andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().string("{\"response\":null,\"data\":null}"));

    }
    @Test
    void testGetBookingHistory() throws Exception {
        when(ticketDetailService.myBookingTickets(Mockito.<Long>any(), Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<String>any(), anyInt(), anyInt())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/ticket/mybooking/{userId}/{status}/{from}/{to}/{pageIndex}/{pageSize}", 1L, "Status",
                "2023-09-10 13:09:31.000", "2023-09-14 13:09:31.000", 1, 3);
        MockMvcBuilders.standaloneSetup(trainTicketController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testGetByPNRNumber() throws Exception {
        when(ticketDetailService.fetchTicketPNRNumber(Mockito.<String>any(), Mockito.<Long>any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/ticket/fetchbypnr/{userId}/{pnr}", 1L,
                "Pnr");
        MockMvcBuilders.standaloneSetup(trainTicketController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testGetUpComingTickets() throws Exception {
        when(ticketDetailService.fetchUpComingTickets(Mockito.<Long>any(), anyInt(), anyInt()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/ticket/upcoming/{pageIndex}/{pageSize}", 1, 3);
        MockMvcBuilders.standaloneSetup(trainTicketController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }
}

