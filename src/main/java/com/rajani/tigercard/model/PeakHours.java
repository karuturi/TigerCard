package com.rajani.tigercard.model;


import java.time.DayOfWeek;
import java.util.LinkedList;
import java.util.List;

public class PeakHours {
    private PeakHours() {
    }

    public static List<Interval> getPeekHours(DayOfWeek day) {
        List<Interval> intervals = new LinkedList<>();
        switch (day) {
            case SATURDAY:
            case SUNDAY:
                intervals.add(new Interval("09:00", "11:00"));
                intervals.add(new Interval("18:00", "22:00"));
                break;
            default:
                intervals.add(new Interval("07:00", "10:30"));
                intervals.add(new Interval("17:00", "20:00"));
                break;
        }
        return intervals;
    }
}
