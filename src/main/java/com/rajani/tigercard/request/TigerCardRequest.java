package com.rajani.tigercard.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Request class for total fare computation
 * It has only daily ticket requests.
 * Can also have fields like name, gender, dob, profession etc. to calculate additional applicable discounts
 */
@Getter
@AllArgsConstructor
@ToString
public class TigerCardRequest {
//    String name;
//    LocalDate dob;
//    Gender gender;
    List<Ticket> tickets;
}
