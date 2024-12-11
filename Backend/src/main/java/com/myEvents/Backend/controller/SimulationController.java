package com.myEvents.Backend.controller;

import com.myEvents.Backend.dto.SimulationConfigDTO;
import com.myEvents.Backend.dto.SystemStatusDTO;
import com.myEvents.Backend.service.SimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/simulation")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
public class SimulationController {


    private final SimulationService simulationService;

    @Autowired
    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
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
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
