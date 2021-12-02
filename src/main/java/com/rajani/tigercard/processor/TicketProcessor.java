package com.rajani.tigercard.processor;

import com.rajani.tigercard.request.Ticket;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TicketProcessor {
    @Getter
    private int fare;

    private int currentWeekFare;

    private int currentWeekMaxCap;

    private int currentDayFare;
    private int currentDayMaxCap;

    public TicketProcessor() {
        this.fare = 0;
        this.currentDayFare = 0;
        this.currentDayMaxCap = 0;
        this.currentWeekFare = 0;
        this.currentWeekMaxCap = 0;
    }

    public void process(Ticket ticket) {
        log.debug("processing ticket {}", ticket);
        int ticketFare = ticket.getFare();
        fare = fare + ticketFare;
        log.debug("fare after processing ticket {}", fare);
    }
}
