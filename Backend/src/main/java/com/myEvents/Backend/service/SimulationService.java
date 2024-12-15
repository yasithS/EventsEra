package com.myEvents.Backend.service;

import com.myEvents.Backend.dto.SimulationConfigDTO;
import com.myEvents.Backend.dto.SystemStatusDTO;
import com.myEvents.Backend.model.SystemConfiguration;
import com.myEvents.Backend.model.ThreadLog;
import com.myEvents.Backend.repository.ConfigurationRepository;
import com.myEvents.Backend.repository.ThreadLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class SimulationService {

    private final ConfigurationRepository configRepository;
    private final ThreadLogRepository logRepository;
    private TicketPool ticketPool;
    private ExecutorService vendorExecutor;
    private ExecutorService customerExecutor;
    private volatile boolean isSimulationRunning = false;
    private volatile int activeVendorCount = 0;
    private volatile int activeCustomerCount = 0;

    @Autowired
    public SimulationService(ConfigurationRepository configRepository, ThreadLogRepository logRepository) {
        this.configRepository = configRepository;
        this.logRepository = logRepository;
    }

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
        logThreadAction("SYSTEM", "CONFIGURE", "System configured with " + config.getMaxTicketPool() + " tickets");
    }

    public void logThreadAction(String threadType, String action, String message) {
        ThreadLog log = new ThreadLog();
        log.setThreadType(threadType);
        log.setAction(action);
        log.setTimestamp(LocalDateTime.now());
        log.setMessage(message);
        logRepository.save(log);
    }

    public void startSimulation() {
        if (ticketPool == null) {
            throw new IllegalStateException("System must be configured before starting simulation");
        }

        if (isSimulationRunning) {
            throw new IllegalStateException("Simulation is already running");
        }

        SystemConfiguration config = configRepository.findTopByOrderByIdDesc();
        if (config == null) {
            throw new IllegalStateException("No configuration found");
        }

        isSimulationRunning = true;
        activeVendorCount = config.getTotalVendors();
        activeCustomerCount = config.getTotalCustomers();

        vendorExecutor = Executors.newFixedThreadPool(config.getTotalVendors());
        customerExecutor = Executors.newFixedThreadPool(config.getTotalCustomers());

        logThreadAction("SYSTEM", "START", "Starting simulation with " + config.getTotalVendors() +
                " vendors and " + config.getTotalCustomers() + " customers");

        // Start vendor threads
        for (int i = 0; i < config.getTotalVendors() && isSimulationRunning; i++) {
            final int vendorId = i + 1;
            vendorExecutor.execute(new Vendor(
                    config.getReleasePerVendor(),
                    config.getVendorReleaseRate(),
                    ticketPool,
                    logRepository
            ) {
                @Override
                public void run() {
                    try {
                        logThreadAction("VENDOR-" + vendorId, "START", "Vendor thread started");
                        super.run();
                    } finally {
                        activeVendorCount--;
                        logThreadAction("VENDOR-" + vendorId, "STOP", "Vendor thread stopped");
                    }
                }
            });
        }

        // Start customer threads
        for (int i = 0; i < config.getTotalCustomers() && isSimulationRunning; i++) {
            final int customerId = i + 1;
            customerExecutor.execute(new Customer(
                    ticketPool,
                    config.getCustomerBuyingRate(),
                    config.getTicketsPerCustomer()
            ) {
                @Override
                public void run() {
                    try {
                        logThreadAction("CUSTOMER-" + customerId, "START", "Customer thread started");
                        super.run();
                    } finally {
                        activeCustomerCount--;
                        logThreadAction("CUSTOMER-" + customerId, "STOP", "Customer thread stopped");
                    }
                }
            });
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
        activeVendorCount = 0;
        activeCustomerCount = 0;
        logThreadAction("SYSTEM", "STOP", "Simulation stopped manually");
    }

    public SystemStatusDTO getSystemStatus() {
        if (ticketPool == null) {
            SystemStatusDTO status = new SystemStatusDTO();
            status.setMaximumPoolSize(0);
            status.setCurrentPoolSize(0);
            status.setAvailableSpace(0);
            status.setTotalTicketsReleased(0);
            status.setTotalTicketsBought(0);
            status.setActiveVendorThreads(0);
            status.setActiveCustomerThreads(0);
            return status;
        }

        SystemStatusDTO status = new SystemStatusDTO();

        status.setMaximumPoolSize(Math.max(0, ticketPool.getMaximumTicketCount()));
        status.setCurrentPoolSize(Math.max(0, ticketPool.getCurrentTicketCount()));
        status.setAvailableSpace(Math.max(0, ticketPool.getMaximumTicketCount() - ticketPool.getCurrentTicketCount()));
        status.setTotalTicketsReleased(Math.max(0, ticketPool.getTotalTicketsReleased()));
        status.setTotalTicketsBought(Math.max(0, ticketPool.getTotalTicketsBought()));
        status.setActiveVendorThreads(Math.max(0, activeVendorCount));
        status.setActiveCustomerThreads(Math.max(0, activeCustomerCount));

        return status;
    }
}