package com.myEvents.Backend.service;

import com.myEvents.Backend.model.Ticket;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;

@Component
public class TicketPool {
    private int maximumTicketCount;
    private Queue<Ticket> ticketQueue;
    private volatile int totalTicketsReleased;
    private volatile int totalTicketsBought;

    public TicketPool() {
        this.ticketQueue = new LinkedList<>();
    }

    public TicketPool(int maximumTicketCount) {
        this.maximumTicketCount = maximumTicketCount;
        this.ticketQueue = new LinkedList<>();
    }

    public synchronized void addTicket(Ticket ticket) {
        while (ticketQueue.size() >= maximumTicketCount) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        ticketQueue.add(ticket);
        totalTicketsReleased++;
        notifyAll();
    }

    public synchronized Ticket removeTicket() {
        while (ticketQueue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        Ticket ticket = ticketQueue.poll();
        totalTicketsBought++;
        notifyAll();
        return ticket;
    }
    public void setMaximumTicketCount(int maximumTicketCount) {
        this.maximumTicketCount = maximumTicketCount;
    }

    public synchronized int getCurrentTicketCount() {
        return ticketQueue.size();
    }

    public synchronized int getTotalTicketsReleased() {
        return totalTicketsReleased;
    }

    public synchronized int getTotalTicketsBought() {
        return totalTicketsBought;
    }

    public synchronized int getMaximumTicketCount() {
        return maximumTicketCount;
    }
}