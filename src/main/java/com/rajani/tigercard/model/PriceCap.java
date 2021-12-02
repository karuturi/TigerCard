package com.rajani.tigercard.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PriceCap {
    ONE_ONE(100, 500),
    ONE_TWO(120, 600),
    TWO_TWO(80, 400);

    private int dailyCap;
    private int weeklyCap;

    public static PriceCap of(Zone fromZone, Zone toZone) {
        if (fromZone == Zone.ONE && toZone == Zone.ONE) {
            return PriceCap.ONE_ONE;
        } else if (fromZone == Zone.TWO && toZone == Zone.TWO) {
            return PriceCap.TWO_TWO;
        } else {
            return PriceCap.ONE_TWO;
        }
    }
}
