package com.rajani.tigercard.processor;

import com.rajani.tigercard.model.Zone;
import com.rajani.tigercard.request.Ticket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

class TicketProcessorTest {

    TicketProcessor ticketProcessor;

    public static Stream<Arguments> TickerProviderPerDayUnderDailyCap() {
        return Stream.of(
                Arguments.of(Arrays.asList(
                    new Ticket(LocalDateTime.parse("2007-12-01T10:15:30"), Zone.ONE, Zone.ONE)
                    ), 30
                ),
                Arguments.of(Arrays.asList(
                    new Ticket(LocalDateTime.parse("2007-12-01T04:15:30"), Zone.ONE, Zone.ONE)
                    ), 25
                ),
                Arguments.of(Arrays.asList(
                    new Ticket(LocalDateTime.parse("2007-12-01T10:15:30"), Zone.ONE, Zone.ONE),
                    new Ticket(LocalDateTime.parse("2007-12-01T04:15:30"), Zone.ONE, Zone.ONE)
                    ), 55
                )
        );
    }

    @BeforeEach
    void setUp() {
        ticketProcessor = new TicketProcessor();
    }

    @DisplayName("test to process fare for a day under daily cap")
    @ParameterizedTest(name = "{index} ==> test data")
    @MethodSource("TickerProviderPerDayUnderDailyCap")
    void processTicketsPerDayUnderDailyLimit(List<Ticket> tickets, int expectedFare) {
        tickets.forEach( ticket -> ticketProcessor.process(ticket));
        Assertions.assertEquals(expectedFare, ticketProcessor.getFare());

    }

    @AfterEach
    void tearDown() {
        ticketProcessor = null;
    }
}