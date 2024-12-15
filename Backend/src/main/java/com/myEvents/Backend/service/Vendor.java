package com.myEvents.Backend.service;

import com.myEvents.Backend.model.ThreadLog;
import com.myEvents.Backend.model.Ticket;
import com.myEvents.Backend.repository.ThreadLogRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class Vendor implements Runnable {
    private final int totalTickets;
    private final int ticketReleaseRate;
    private final TicketPool ticketPool;
    private final ThreadLogRepository logRepository;

    public Vendor(int totalTickets, int ticketReleaseRate, TicketPool ticketPool,ThreadLogRepository logRepository) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.ticketPool = ticketPool;
        this.logRepository = logRepository;
    }

    private void logAction(String action, String message) {
        ThreadLog log = new ThreadLog();
        log.setThreadType("VENDOR");
        log.setAction(action);
        log.setTimestamp(LocalDateTime.now());
        log.setMessage(message);
        logRepository.save(log);
    }

    @Override
    public void run() {
        logAction("STARTED", "Vendor thread started");
        try {
            for (int i = 0; i < totalTickets && !Thread.currentThread().isInterrupted(); i++) {
                Ticket ticket = new Ticket();
                ticket.setEventName("API_KAWURUDA_WAYO");
                ticket.setTicketPrice(new BigDecimal("3000"));
                ticketPool.addTicket(ticket);
                logAction("RELEASED", "Vendor released ticket #" + (i + 1));
                Thread.sleep(ticketReleaseRate * 1000L);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logAction("INTERRUPTED", "Vendor thread interrupted");
        }
        logAction("COMPLETED", "Vendor thread completed");
    }
}