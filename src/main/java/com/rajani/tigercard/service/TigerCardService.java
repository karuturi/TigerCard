package com.rajani.tigercard.service;

import com.rajani.tigercard.processor.TicketProcessor;
import com.rajani.tigercard.request.Ticket;
import com.rajani.tigercard.request.TigerCardRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TigerCardService {
    /**
     * This method computes the fare of the given request, taking into account daily and weekly pass
     *
     * @param tigerCardRequest tigerCardRequest containing daily tickets and user info
     * @return computed fare (assuming fare is always an integer)
     */
    public int computeFare(TigerCardRequest tigerCardRequest) {
        log.debug("started processing tigercard request {}", tigerCardRequest);
        TicketProcessor ticketProcessor = new TicketProcessor();
        //expects the tickets in time order. If not it needs to be sorted here
//        List<Ticket> sortedRequests = tigerCardRequest.getTickets();
//        sortedRequests.sort(Comparator.comparing(Ticket::getDateTime));

        for (Ticket ticket : tigerCardRequest.getTickets()) {
            ticketProcessor.process(ticket);
        }
        return ticketProcessor.getFare();
    }
}
