package com.rajani.tigercard.processor;

import com.rajani.tigercard.model.Zone;
import com.rajani.tigercard.request.Ticket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

class TicketProcessorTest {

    TicketProcessor ticketProcessor;

    public static Stream<Arguments> ticketsProviderPerDayUnderDailyCap() {
        return Stream.of(
                Arguments.of(new Ticket(LocalDateTime.parse("2007-12-01T10:15:30"), Zone.ONE, Zone.ONE), 30),
                Arguments.of(new Ticket(LocalDateTime.parse("2007-12-01T04:15:30"), Zone.ONE, Zone.ONE), 25),
                Arguments.of(new Ticket(LocalDateTime.parse("2021-11-29T10:10"), Zone.TWO, Zone.ONE), 35),
                Arguments.of(new Ticket(LocalDateTime.parse("2021-11-29T04:10"), Zone.ONE, Zone.ONE), 25),
                Arguments.of(new Ticket(LocalDateTime.parse("2021-11-29T16:45"), Zone.ONE, Zone.ONE), 25),
                Arguments.of(new Ticket(LocalDateTime.parse("2021-11-29T18:15"), Zone.ONE, Zone.ONE), 30),
                Arguments.of(new Ticket(LocalDateTime.parse("2021-11-29T19:00"), Zone.ONE, Zone.TWO), 35)
        );
    }


    @BeforeEach
    void setUp() {
        ticketProcessor = new TicketProcessor();
    }

    @DisplayName("test to process fare for a day under daily cap")
    @ParameterizedTest(name = "{index} ==> test data")
    @MethodSource("ticketsProviderPerDayUnderDailyCap")
    void processTicketsPerDayUnderDailyLimit(Ticket ticket, int expectedFare) {
        ticketProcessor.process(ticket);
        Assertions.assertEquals(expectedFare, ticketProcessor.getFare());

    }

    @DisplayName("test to process fare for a day over daily cap")
    @Test
    void processTicketsPerDayOverDailyLimit() {
//        ticketProcessor.setCurrentDay(LocalDate.parse("2021-11-29").getDayOfYear(), 0);
        Assertions.assertEquals(0, ticketProcessor.getFare());
        ticketProcessor.process(new Ticket(LocalDateTime.parse("2021-11-29T10:10"), Zone.TWO, Zone.ONE));
        Assertions.assertEquals(35, ticketProcessor.getFare());
        ticketProcessor.process(new Ticket(LocalDateTime.parse("2021-11-29T04:10"), Zone.ONE, Zone.ONE));
        Assertions.assertEquals(60, ticketProcessor.getFare());
        ticketProcessor.process(new Ticket(LocalDateTime.parse("2021-11-29T16:45"), Zone.ONE, Zone.ONE));
        Assertions.assertEquals(85, ticketProcessor.getFare());
        ticketProcessor.process(new Ticket(LocalDateTime.parse("2021-11-29T18:15"), Zone.ONE, Zone.ONE));
        Assertions.assertEquals(115, ticketProcessor.getFare());
        ticketProcessor.process(new Ticket(LocalDateTime.parse("2021-11-29T19:00"), Zone.ONE, Zone.TWO));
        Assertions.assertEquals(120, ticketProcessor.getFare());
        //after cap reached
        ticketProcessor.process(new Ticket(LocalDateTime.parse("2021-11-29T20:00"), Zone.ONE, Zone.TWO));
        Assertions.assertEquals(120, ticketProcessor.getFare());
        //next day
        ticketProcessor.process(new Ticket(LocalDateTime.parse("2021-11-30T19:00"), Zone.ONE, Zone.TWO));
        Assertions.assertEquals(155, ticketProcessor.getFare());
        //next week and day
        ticketProcessor.process(new Ticket(LocalDateTime.parse("2021-12-06T19:00"), Zone.ONE, Zone.TWO));
        Assertions.assertEquals(190, ticketProcessor.getFare());
    }

    @AfterEach
    void tearDown() {
        ticketProcessor = null;
    }
}