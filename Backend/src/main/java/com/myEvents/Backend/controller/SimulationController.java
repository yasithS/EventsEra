package com.myEvents.Backend.controller;

import com.myEvents.Backend.dto.SimulationConfigDTO;
import com.myEvents.Backend.dto.SystemStatusDTO;
import com.myEvents.Backend.dto.ThreadLogDTO;
import com.myEvents.Backend.model.ThreadLog;
import com.myEvents.Backend.repository.ThreadLogRepository;
import com.myEvents.Backend.service.SimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/simulation")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
public class SimulationController {


    private final SimulationService simulationService;
    private final ThreadLogRepository logRepository;

    @Autowired
    public SimulationController(SimulationService simulationService,ThreadLogRepository logRepository) {
        this.simulationService = simulationService;
        this.logRepository = logRepository;
    }

    @PostMapping("/configure")
    public ResponseEntity<Void> configureSystem(@RequestBody SimulationConfigDTO config){
        simulationService.saveConfiguration(config);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/start")
    public ResponseEntity<String> startSimulation(){
        try{
            simulationService.startSimulation();
            return ResponseEntity.ok("Simulation Started Successfully");
        }catch (IllegalStateException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopSimulation() {
        try {
            simulationService.stopSimulation();
            return ResponseEntity.ok("Simulation stopped successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/status")
    public ResponseEntity<SystemStatusDTO> getSystemStatus() {
        try {
            SystemStatusDTO status = simulationService.getSystemStatus();
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            System.err.println("Error getting system status: " + e.getMessage());
            SystemStatusDTO emptyStatus = new SystemStatusDTO();
            emptyStatus.setMaximumPoolSize(0);
            emptyStatus.setCurrentPoolSize(0);
            emptyStatus.setAvailableSpace(0);
            emptyStatus.setTotalTicketsReleased(0);
            emptyStatus.setTotalTicketsBought(0);
            emptyStatus.setActiveVendorThreads(0);
            emptyStatus.setActiveCustomerThreads(0);
            return ResponseEntity.ok(emptyStatus);
        }
    }

    @GetMapping("/logs")
    public ResponseEntity<List<ThreadLogDTO>> getLogs() {
        try {
            List<ThreadLog> logs = logRepository.findTop50ByOrderByTimestampDesc();
            List<ThreadLogDTO> logDTOs = logs.stream()
                    .map(log -> {
                        ThreadLogDTO dto = new ThreadLogDTO();
                        dto.setId(log.getId());
                        dto.setThreadType(log.getThreadType());
                        dto.setAction(log.getAction());
                        dto.setTimestamp(log.getTimestamp());
                        dto.setMessage(log.getMessage());
                        return dto;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(logDTOs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
