package com.rajani.tigercard.service;

import com.rajani.tigercard.model.Zone;
import com.rajani.tigercard.request.Ticket;
import com.rajani.tigercard.request.TigerCardRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Stream;


class TigerCardServiceTest {

    TigerCardService tigerCardService;

    @BeforeEach
    void setUp() {
        tigerCardService = new TigerCardService();
    }

    public static Stream<Arguments> tigerCardRequestProviderPerDayUnderDailyCap() {
        return Stream.of(
                Arguments.of(Named.of("test zone 1 weekend", new TigerCardRequest(Arrays.asList(
                                new Ticket(LocalDateTime.parse("2007-12-01T10:15:30"), Zone.ONE, Zone.ONE),
                                new Ticket(LocalDateTime.parse("2007-12-01T04:15:30"), Zone.ONE, Zone.ONE)
                        ))), 55
                ),
                Arguments.of(Named.of("test zone 2 non peak hours weekday", new TigerCardRequest(Arrays.asList(
                                new Ticket(LocalDateTime.parse("2021-12-04T11:00:00"), Zone.TWO, Zone.TWO),
                                new Ticket(LocalDateTime.parse("2021-12-04T23:15:30"), Zone.TWO, Zone.TWO)
                        ))), 40
                ),
                Arguments.of(Named.of("test zone 1 and 2 peak hours weekday", new TigerCardRequest(Arrays.asList(
                                new Ticket(LocalDateTime.parse("2021-12-01T10:10:00"), Zone.ONE, Zone.TWO),
                                new Ticket(LocalDateTime.parse("2021-12-01T17:30:00"), Zone.TWO, Zone.ONE)
                        ))), 70
                )
        );
    }

    @DisplayName("test to compute fare for a day under daily cap")
    @ParameterizedTest(name = "{index} ==> test data {0}")
    @MethodSource("tigerCardRequestProviderPerDayUnderDailyCap")
    void computeFarePerDay(TigerCardRequest request, int expectedFare) {
        Assertions.assertEquals(expectedFare, tigerCardService.computeFare(request));
    }
}
