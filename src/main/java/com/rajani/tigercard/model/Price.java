package com.rajani.tigercard.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Price {
    ONE_ONE(30, 25),
    TWO_TWO(25, 20),
    ONE_TWO(35, 30);
//    TWO_ONE(35,30);

    private int peakPrice;
    private int offPeakPrice;

    public static Price of(Zone fromZone, Zone toZone) {
        if (fromZone == Zone.ONE && toZone == Zone.ONE) {
            return Price.ONE_ONE;
        } else if (fromZone == Zone.TWO && toZone == Zone.TWO) {
            return Price.TWO_TWO;
        } else {
            return Price.ONE_TWO;
        }
    }
}
