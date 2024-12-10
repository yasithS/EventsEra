package com.myEvents.Backend.service;

import com.myEvents.Backend.dto.SimulationConfigDTO;
import com.myEvents.Backend.dto.SystemStatusDTO;
import com.myEvents.Backend.model.SystemConfiguration;
import com.myEvents.Backend.repository.ConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
// @RequiredArgsConstructor
public class SimulationService {

    private final ConfigurationRepository configRepository;

    @Autowired
    public SimulationService(ConfigurationRepository configRepository) {
        this.configRepository = configRepository;
    }
    private TicketPool ticketPool;
    private ExecutorService vendorExecutor;
    private ExecutorService customerExecutor;
    private volatile boolean isSimulationRunning = false;

    public void saveConfiguration(SimulationConfigDTO config) {
        SystemConfiguration systemConfig = new SystemConfiguration();
        systemConfig.setMaxTicketPool(config.getMaxTicketPool());
        systemConfig.setTotalVendors(config.getTotalVendors());
        systemConfig.setReleasePerVendor(config.getReleasePerVendor());
        systemConfig.setVendorReleaseRate(config.getVendorReleaseRate());
        systemConfig.setTotalCustomers(config.getTotalCustomers());
        systemConfig.setTicketsPerCustomer(config.getTicketsPerCustomer());
        systemConfig.setCustomerBuyingRate(config.getCustomerBuyingRate());

        configRepository.save(systemConfig);

        ticketPool = new TicketPool();
        ticketPool.setMaximumTicketCount(config.getMaxTicketPool());

    }

    public void startSimulation() {
        if (ticketPool == null) {
            throw new IllegalStateException("System must be configured before starting simulation");
        }

        if (isSimulationRunning) {
            throw new IllegalStateException("Simulation is already running");
        }

        SystemConfiguration config = configRepository.findTopByOrderByIdDesc();
        isSimulationRunning = true;

        vendorExecutor = Executors.newFixedThreadPool(config.getTotalVendors());
        customerExecutor = Executors.newFixedThreadPool(config.getTotalCustomers());

        // Start vendor threads
        for (int i = 0; i < config.getTotalVendors(); i++) {
            vendorExecutor.execute(new Vendor(
                    config.getReleasePerVendor(),
                    config.getVendorReleaseRate(),
                    ticketPool
            ));
        }

        // Start customer threads
        for (int i = 0; i < config.getTotalCustomers(); i++) {
            customerExecutor.execute(new Customer(
                    ticketPool,
                    config.getCustomerBuyingRate(),
                    config.getTicketsPerCustomer()
            ));
        }
    }

    public void stopSimulation() {
        if (!isSimulationRunning) {
            throw new IllegalStateException("No simulation is currently running");
        }

        isSimulationRunning = false;
        if (vendorExecutor != null) {
            vendorExecutor.shutdownNow();
        }
        if (customerExecutor != null) {
            customerExecutor.shutdownNow();
        }
    }

    public SystemStatusDTO getSystemStatus() {
        if (ticketPool == null) {
            throw new IllegalStateException("System not configured");
        }

        SystemStatusDTO status = new SystemStatusDTO();
        status.setMaximumPoolSize(ticketPool.getMaximumTicketCount());
        status.setCurrentPoolSize(ticketPool.getCurrentTicketCount());
        status.setAvailableSpace(ticketPool.getMaximumTicketCount() - ticketPool.getCurrentTicketCount());
        status.setTotalTicketsReleased(ticketPool.getTotalTicketsReleased());
        status.setTotalTicketsBought(ticketPool.getTotalTicketsBought());

        return status;
    }


}
