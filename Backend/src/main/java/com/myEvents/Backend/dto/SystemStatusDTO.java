package com.myEvents.Backend.dto;

import lombok.Data;

@Data
public class SystemStatusDTO {
    private int maximumPoolSize;
    private int currentPoolSize;
    private int availableSpace;
    private int totalTicketsReleased;
    private int totalTicketsBought;
    private int activeVendorThreads;
    private int activeCustomerThreads;

    public void setMaximumPoolSize(int maximumPoolSize) { this.maximumPoolSize = maximumPoolSize; }
    public void setCurrentPoolSize(int currentPoolSize) { this.currentPoolSize = currentPoolSize; }
    public void setAvailableSpace(int availableSpace) { this.availableSpace = availableSpace; }
    public void setTotalTicketsReleased(int totalTicketsReleased) { this.totalTicketsReleased = totalTicketsReleased; }
    public void setTotalTicketsBought(int totalTicketsBought) { this.totalTicketsBought = totalTicketsBought; }
    public void setActiveVendorThreads(int activeVendorThreads) { this.activeVendorThreads = activeVendorThreads; }
    public void setActiveCustomerThreads(int activeCustomerThreads) { this.activeCustomerThreads = activeCustomerThreads; }
}
