package com.rajani.tigercard.processor;

import com.rajani.tigercard.model.PriceCap;
import com.rajani.tigercard.request.Ticket;
import lombok.extern.slf4j.Slf4j;

import java.time.temporal.WeekFields;

@Slf4j
public class WeeklyTicketProcessor {

    private int currentWeekFare;
    private int currentWeekMaxCap;
    private int currentWeekOfYear;

    public WeeklyTicketProcessor(int currentWeekFare, int currentWeekMaxCap, int currentWeekOfYear) {
        this.currentWeekFare = currentWeekFare;
        this.currentWeekMaxCap = currentWeekMaxCap;
        this.currentWeekOfYear = currentWeekOfYear;
    }

    public int process(Ticket ticket) {
        setWeeklyMaxCap(ticket);
        int weekOfYear = ticket.getDateTime().get(WeekFields.ISO.weekOfWeekBasedYear());
        int fare = ticket.getFare();
        if (weekOfYear == currentWeekOfYear) {
            log.debug("same week of the year {}", weekOfYear);
            fare = getFareAfterWeeklyCap(fare);
        } else {
            log.debug("current week {} is different from new week {}", currentWeekOfYear, weekOfYear);
            log.debug("resetting weekly fare for a new week");
            resetWeekFare(); // first ticket of new week. ticket fare is always less than cap
        }
        currentWeekOfYear = weekOfYear;
        return fare;
    }

    /**
     * this method exists only for testing convenience. Can use reflection to achieve the same
     */
    protected void setCurrentWeek(int weekOfYear, int weekFare) {
        currentWeekFare = weekFare;
        currentWeekOfYear = weekOfYear;
    }

    private void setWeeklyMaxCap(Ticket ticket) {
        PriceCap priceCap = PriceCap.of(ticket.getFromZone(), ticket.getToZone());
        log.debug("current weekly max cap {}", currentWeekMaxCap);
        currentWeekMaxCap = Math.max(priceCap.getWeeklyCap(), currentWeekMaxCap);
        log.debug("weekly max cap {} after processing", currentWeekMaxCap);
    }
    private int getFareAfterWeeklyCap(int ticketFare) {
        log.debug("current week fare: {}", currentWeekFare);
        if (currentWeekMaxCap < currentWeekFare + ticketFare) {
            return currentWeekMaxCap - currentWeekFare;
        }
        return ticketFare;
    }

    public void incrementCurrentWeekFare(int fare) {
        this.currentWeekFare = currentWeekFare + fare;
    }

    private void resetWeekFare() {
        currentWeekFare = 0;
    }
}

