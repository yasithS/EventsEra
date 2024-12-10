package com.myEvents.Backend.dto;

import lombok.Data;

@Data
public class SimulationConfigDTO {
    private int maxTicketPool;
    private int totalVendors;
    private int releasePerVendor;
    private int vendorReleaseRate;
    private int totalCustomers;
    private int ticketsPerCustomer;
    private int customerBuyingRate;

    public int getMaxTicketPool() { return maxTicketPool; }
    public int getTotalVendors() { return totalVendors; }
    public int getReleasePerVendor() { return releasePerVendor; }
    public int getVendorReleaseRate() { return vendorReleaseRate; }
    public int getTotalCustomers() { return totalCustomers; }
    public int getTicketsPerCustomer() { return ticketsPerCustomer; }
    public int getCustomerBuyingRate() { return customerBuyingRate; }
}
