package com.rajani.tigercard.processor;

import com.rajani.tigercard.model.PriceCap;
import com.rajani.tigercard.request.Ticket;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DailyTicketProcessor {
    private int currentDayFare;
    private int currentDayMaxCap;
    private int currentDayOfYear;

    public DailyTicketProcessor(int currentDayFare, int currentDayMaxCap, int currentDayOfYear) {
        this.currentDayFare = currentDayFare;
        this.currentDayMaxCap = currentDayMaxCap;
        this.currentDayOfYear = currentDayOfYear;
    }

    public int process(Ticket ticket) {
        setDailyMaxCap(ticket);
        int dayOfYear = ticket.getDateTime().getDayOfYear();
        int fare = ticket.getFare();
        if (dayOfYear == currentDayOfYear) {
            log.debug("same day of the year {}", dayOfYear);
            fare = processSameDayTicket(fare);
        } else {
            log.debug("current day {} is different from new day {}", currentDayOfYear, dayOfYear);
            resetDayFare();
        }
        currentDayOfYear = dayOfYear;
        return fare;
    }

    public void incrementCurrentDayFare(int fare) {
        currentDayFare = currentDayFare + fare;
    }

    /**
     * this method exists only for testing convenience. Can use reflection to achieve the same
     */
    protected void setCurrentDay(int dayOfYear, int dayFare) {
        currentDayFare = dayFare;
        currentDayOfYear = dayOfYear;
    }

    private int processSameDayTicket(int ticketFare) {
        if (currentDayMaxCap < currentDayFare + ticketFare) {
            return currentDayMaxCap - currentDayFare;
        }
        return ticketFare;
    }

    private void setDailyMaxCap(Ticket ticket) {
        PriceCap priceCap = PriceCap.of(ticket.getFromZone(), ticket.getToZone());
        log.debug("current daily max CAP {} ", currentDayMaxCap);
        currentDayMaxCap = Math.max(priceCap.getDailyCap(), currentDayMaxCap);
        log.debug("daily max CAP {}", currentDayMaxCap);
    }

    private void resetDayFare() {
        currentDayFare = 0;
    }
}
