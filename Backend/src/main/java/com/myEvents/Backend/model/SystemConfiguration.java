package com.myEvents.Backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class SystemConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int maxTicketPool;
    private int totalVendors;
    private int releasePerVendor;
    private int vendorReleaseRate;
    private int totalCustomers;
    private int ticketsPerCustomer;
    private int customerBuyingRate;

    public int getTotalVendors() { return totalVendors; }
    public int getReleasePerVendor() { return releasePerVendor; }
    public int getVendorReleaseRate() { return vendorReleaseRate; }
    public int getCustomerBuyingRate() { return customerBuyingRate; }
    public int getTicketsPerCustomer() { return ticketsPerCustomer; }
    public int getTotalCustomers() { return totalCustomers; }

    public void setMaxTicketPool(int maxTicketPool) { this.maxTicketPool = maxTicketPool; }
    public void setTotalVendors(int totalVendors) { this.totalVendors = totalVendors; }
    public void setReleasePerVendor(int releasePerVendor) { this.releasePerVendor = releasePerVendor; }
    public void setVendorReleaseRate(int vendorReleaseRate) { this.vendorReleaseRate = vendorReleaseRate; }
    public void setTotalCustomers(int totalCustomers) { this.totalCustomers = totalCustomers; }
    public void setTicketsPerCustomer(int ticketsPerCustomer) { this.ticketsPerCustomer = ticketsPerCustomer; }
    public void setCustomerBuyingRate(int customerBuyingRate) { this.customerBuyingRate = customerBuyingRate; }
}
