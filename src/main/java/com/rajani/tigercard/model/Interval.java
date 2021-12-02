package com.rajani.tigercard.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalTime;

@ToString
@AllArgsConstructor
@Getter
public class Interval {
    LocalTime from;
    LocalTime to;

    public Interval(String from, String to) {
        this.from = LocalTime.parse(from);
        this.to = LocalTime.parse(to);
    }
}
