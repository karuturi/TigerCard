package com.rajani.tigercard.processor;

import com.rajani.tigercard.model.PriceCap;
import com.rajani.tigercard.request.Ticket;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.temporal.WeekFields;

@Slf4j
public class TicketProcessor {
    @Getter
    private int fare;

    private DailyTicketProcessor dailyTicketProcessor;
    private WeeklyTicketProcessor weeklyTicketProcessor;

    public TicketProcessor() {
        this.fare = 0;
        this.dailyTicketProcessor = new DailyTicketProcessor(0, 0, 0);
        this.weeklyTicketProcessor = new WeeklyTicketProcessor(0, 0, 0);
    }

    /**
     * this method exists only for testing convenience. Can use reflection to achieve the same
     */
    protected void setCurrentDayAndWeek(int dayOfYear, int dayFare, int weekOfYear, int weekFare, int totalFare) {
        fare = totalFare;
        dailyTicketProcessor.setCurrentDay(dayOfYear, dayFare);
        weeklyTicketProcessor.setCurrentWeek(weekOfYear, weekFare);
    }

    public void process(Ticket ticket) {
        log.debug("processing ticket {}", ticket);
        int dailyFare = dailyTicketProcessor.process(ticket);
        log.debug("daily fare after processing: {}", dailyFare);
        int weeklyFare = weeklyTicketProcessor.process(ticket);
        log.debug("weekly fare after processing: {}", weeklyFare);
        int ticketFare = Math.min(dailyFare, weeklyFare);
        dailyTicketProcessor.incrementCurrentDayFare(ticketFare);
        weeklyTicketProcessor.incrementCurrentWeekFare(ticketFare);
        fare = fare + ticketFare;
        log.debug("fare after processing ticket {}", fare);
    }
}
