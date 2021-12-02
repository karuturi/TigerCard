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

    private int currentWeekFare;
    private int currentWeekMaxCap;
    private int currentDayFare;
    private int currentDayMaxCap;
    private int currentDayOfYear;
    private int currentWeekOfYear;

    public TicketProcessor() {
        this.fare = 0;
        this.currentDayFare = 0;
        this.currentDayMaxCap = 0;
        this.currentWeekFare = 0;
        this.currentWeekMaxCap = 0;
        this.currentDayOfYear = 0;
        this.currentWeekOfYear = 0;
    }

    /**
     * this method exists only for testing convenience. Can use reflection to achieve the same
     */
    protected void setCurrentWeek(int weekOfYear, int weekFare) {
        currentWeekFare = weekFare;
        currentWeekOfYear = weekOfYear;
    }

    /**
     * this method exists only for testing convenience. Can use reflection to achieve the same
     */
    protected void setCurrentDay(int dayOfYear, int dayFare) {
        currentDayFare = dayFare;
        currentDayOfYear = dayOfYear;
    }
    public void process(Ticket ticket) {
        log.debug("processing ticket {}", ticket);
        int ticketFare = ticket.getFare();
        int dayOfYear = ticket.getDateTime().getDayOfYear();
        int weekOfYear = ticket.getDateTime().get(WeekFields.ISO.weekOfWeekBasedYear());
        PriceCap priceCap = PriceCap.of(ticket.getFromZone(), ticket.getToZone());
        currentDayMaxCap = Math.max(priceCap.getDailyCap(), currentDayMaxCap);
        currentWeekMaxCap = Math.max(priceCap.getWeeklyCap(), currentWeekMaxCap);
        if(dayOfYear == currentDayOfYear) {
            log.debug("same day of the year {}", dayOfYear);
            ticketFare = processSameDayTicket(ticketFare);
        } else {
            log.debug("current day {} is different from new day {}", currentDayOfYear, dayOfYear);
            ticketFare = processDifferentDayTicket(ticketFare, weekOfYear);
        }
        incrementWeekFare(ticketFare);
        incrementDayFare(ticketFare);
        //update current day and total fare
        currentDayOfYear = dayOfYear;
        currentWeekOfYear = weekOfYear;
        fare = fare + ticketFare;
        log.debug("fare after processing ticket {}", fare);
    }

    private int processDifferentDayTicket(int ticketFare, int weekOfYear) {
        log.debug("resetting daily fare for a different day.");
        resetDayFare(); //first ticket of the day. ticket fare is always less than cap
        int deltaFare = ticketFare;
        if(weekOfYear == currentWeekOfYear) {
            log.debug("same week of the year {}", weekOfYear);
            deltaFare = getFareAfterWeeklyCap(deltaFare);
        } else {
            log.debug("current week {} is different from new week {}", currentWeekOfYear, weekOfYear);
            log.debug("resetting weekly fare for a new week");
            resetWeekFare(); // first ticket of new week. ticket fare is always less than cap
        }
        return deltaFare;
    }

    private int processSameDayTicket(int ticketFare) {
        int deltaFare = getFareAfterMaxDailyCap(ticketFare);
        deltaFare = getFareAfterWeeklyCap(deltaFare);
        return deltaFare;
    }

    private void resetWeekFare() {
        currentWeekFare = 0;
    }

    private void incrementWeekFare(int ticketFare) {
        currentWeekFare = currentWeekFare + ticketFare;
    }

    private void resetDayFare() {
        currentDayFare = 0;
    }

    private void incrementDayFare(int ticketFare) {
        currentDayFare = currentDayFare + ticketFare;
    }

    private int getFareAfterWeeklyCap(int ticketFare) {
        if(currentWeekMaxCap < currentWeekFare+ticketFare) {
            return currentWeekMaxCap - currentWeekFare;
        }
        return ticketFare;
    }

    private int getFareAfterMaxDailyCap(int ticketFare) {
        if(currentDayMaxCap < currentDayFare+ ticketFare) {
            return currentDayMaxCap - currentDayFare;
        }
        return ticketFare;
    }
}
