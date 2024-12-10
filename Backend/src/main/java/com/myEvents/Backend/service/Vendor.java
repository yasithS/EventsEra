package com.myEvents.Backend.service;

import com.myEvents.Backend.model.Ticket;
import lombok.RequiredArgsConstructor;
import java.math.BigDecimal;


public class Vendor implements Runnable {
    private final int totalTickets;
    private final int ticketReleaseRate;
    private final TicketPool ticketPool;

    public Vendor(int totalTickets, int ticketReleaseRate, TicketPool ticketPool) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.ticketPool = ticketPool;
    }

    @Override
    public void run() {
        for (int i = 0; i < totalTickets && !Thread.currentThread().isInterrupted(); i++) {
            Ticket ticket = new Ticket();
            ticket.setEventName("API_KAWURUDA_WAYO");
            ticket.setTicketPrice(new BigDecimal("3000"));
            ticketPool.addTicket(ticket);
            try {
                Thread.sleep(ticketReleaseRate * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}