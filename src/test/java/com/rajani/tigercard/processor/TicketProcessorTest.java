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
import java.time.temporal.WeekFields;
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

    private void setTicketProcessorContext(String date, int dayFare, int weekFare, int totalFare) {
        LocalDate currentDay = LocalDate.parse(date);
        int currentDayOfYear = currentDay.getDayOfYear();
        int currentWeekOfYear = currentDay.get(WeekFields.ISO.weekOfWeekBasedYear());
        ticketProcessor.setCurrentDayAndWeek(currentDayOfYear, dayFare, currentWeekOfYear, weekFare, totalFare);
        Assertions.assertEquals(totalFare, ticketProcessor.getFare());
    }

    @DisplayName("ticket under daily limit")
    @Test
    void ticketUnderDailyLimit() {
        setTicketProcessorContext("2021-12-03", 15, 15, 50);
        ticketProcessor.process(new Ticket(LocalDateTime.parse("2021-12-03T19:00"), Zone.ONE, Zone.TWO));
        Assertions.assertEquals(85, ticketProcessor.getFare());
    }

    @DisplayName("ticket when and after daily limit")
    @Test
    void ticketAfterDailyLimit() {
        setTicketProcessorContext("2021-12-03", 115, 215, 235);
        ticketProcessor.process(new Ticket(LocalDateTime.parse("2021-12-03T10:01"), Zone.ONE, Zone.TWO));
        Assertions.assertEquals(240, ticketProcessor.getFare());
        ticketProcessor.process(new Ticket(LocalDateTime.parse("2021-12-03T17:01"), Zone.ONE, Zone.TWO));
        Assertions.assertEquals(240, ticketProcessor.getFare());
    }

    @DisplayName("ticket under weekly limit same week")
    @Test
    void ticketUnderWeeklyLimitSameWeek() {
        setTicketProcessorContext("2021-12-03", 15, 215, 235);
        ticketProcessor.process(new Ticket(LocalDateTime.parse("2021-12-04T10:01"), Zone.ONE, Zone.TWO));
        Assertions.assertEquals(270, ticketProcessor.getFare());
        ticketProcessor.process(new Ticket(LocalDateTime.parse("2021-12-05T10:01"), Zone.ONE, Zone.TWO));
        Assertions.assertEquals(305, ticketProcessor.getFare());
    }

    @DisplayName("ticket under weekly limit different week")
    @Test
    void ticketUnderWeeklyLimitDifferentWeek() {
        setTicketProcessorContext("2021-12-03", 15, 215, 235);
        ticketProcessor.process(new Ticket(LocalDateTime.parse("2021-12-06T10:01"), Zone.ONE, Zone.TWO));
        Assertions.assertEquals(270, ticketProcessor.getFare());
        ticketProcessor.process(new Ticket(LocalDateTime.parse("2021-12-08T10:01"), Zone.ONE, Zone.TWO));
        Assertions.assertEquals(305, ticketProcessor.getFare());
    }

    @DisplayName("ticket when weekly limit reached without daily limit")
    @Test
    void ticketWeeklyLimitWithoutDailyLimit() {
        setTicketProcessorContext("2021-12-03", 15, 585, 720);
        ticketProcessor.process(new Ticket(LocalDateTime.parse("2021-12-04T10:01"), Zone.ONE, Zone.TWO));
        Assertions.assertEquals(735, ticketProcessor.getFare());
        ticketProcessor.process(new Ticket(LocalDateTime.parse("2021-12-05T10:01"), Zone.ONE, Zone.TWO));
        Assertions.assertEquals(735, ticketProcessor.getFare());
    }

    @DisplayName("ticket when weekly limit reached with daily limit")
    @Test
    void ticketWeeklyLimitWithDailyLimit() {
        setTicketProcessorContext("2021-12-03", 110, 585, 720);
        ticketProcessor.process(new Ticket(LocalDateTime.parse("2021-12-03T10:01"), Zone.ONE, Zone.TWO));
        Assertions.assertEquals(730, ticketProcessor.getFare());
        ticketProcessor.process(new Ticket(LocalDateTime.parse("2021-12-03T11:01"), Zone.ONE, Zone.TWO));
        Assertions.assertEquals(730, ticketProcessor.getFare());
        ticketProcessor.process(new Ticket(LocalDateTime.parse("2021-12-04T11:01"), Zone.ONE, Zone.TWO));
        Assertions.assertEquals(735, ticketProcessor.getFare());
    }

    @AfterEach
    void tearDown() {
        ticketProcessor = null;
    }
}