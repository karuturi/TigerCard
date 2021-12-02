package com.rajani.tigercard.request;

import com.rajani.tigercard.model.Interval;
import com.rajani.tigercard.model.PeakHours;
import com.rajani.tigercard.model.Price;
import com.rajani.tigercard.model.Zone;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * This object has each request details.
 * Assumes the date is without any timezone (hence the use of {@link java.time.LocalDateTime})
 */
@Getter
@AllArgsConstructor
@ToString
public class Ticket {
    LocalDateTime dateTime;
    Zone fromZone;
    Zone toZone;

    /**
     * check if the ticket is in the peek hours of the day
     *
     * @return return true if the ticket is in the peek hour range of the day
     */
    private boolean isPeekHours() {
        boolean isPeekHours = false;
        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
        LocalTime time = dateTime.toLocalTime();
        for (Interval interval : PeakHours.getPeekHours(dayOfWeek)) {
            if (isInRange(time, interval)) {
                isPeekHours = true;
            }
        }
        return isPeekHours;
    }

    /**
     * checks if the time is in the range of from-to excluding from and to
     *
     * @param time     given time
     * @param interval time range (exclusive)
     * @return true if time is in the interval (exclusive)
     */
    private boolean isInRange(LocalTime time, Interval interval) {
        return time.isAfter(interval.getFrom()) && time.isBefore(interval.getTo());
    }

    /**
     * get the ticket fare based on peak or off peak hours
     * this does not include daily cap
     *
     * @return ticket fare
     */
    public int getFare() {
        Price dailyPrice = Price.of(fromZone, toZone);
        if (isPeekHours()) {
            return dailyPrice.getPeakPrice();
        } else {
            return dailyPrice.getOffPeakPrice();
        }
    }
}
