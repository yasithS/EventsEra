import { Component, OnInit } from '@angular/core';
import { SimulationService } from '../../services/simulation.service';

interface SystemStatus {
  maximumPoolSize: number;
  currentPoolSize: number;
  availableSpace: number;
  totalTicketsReleased: number;
  totalTicketsBought: number;
  activeVendorThreads: number;
  activeCustomerThreads: number;
}

@Component({
  selector: 'app-ticket-dashboard',
  templateUrl: './ticket-dashboard.component.html',
  styleUrls: ['./ticket-dashboard.component.scss']
})
export class TicketDashboardComponent implements OnInit {
  status: SystemStatus;
  isRunning = false;
  isLoading = false;
  error:string | null = null;

  constructor(private simulationService: SimulationService) {
    this.status = {
      maximumPoolSize: 0,
      currentPoolSize: 0,
      availableSpace: 0,
      totalTicketsReleased: 0,
      totalTicketsBought: 0,
      activeVendorThreads: 0,
      activeCustomerThreads: 0
    };
  }

  ngOnInit(): void {
    this.fetchStatus();
  }

  startSimulation(): void {
    this.isLoading = true;
    this.simulationService.startSimulation().subscribe({
      next: () => {
        this.isRunning = true;
        this.startStatusPolling();
      },
      error: (error) => console.error('Error starting simulation:', error),
      complete: () => this.isLoading = false
    });
  }

  stopSimulation(): void {
    this.isLoading = true;
    this.simulationService.stopSimulation().subscribe({
      next: () => {
        this.isRunning = false;
        this.fetchStatus();
      },
      error: (error) => console.error('Error stopping simulation:', error),
      complete: () => this.isLoading = false
    });
  }

  onConfigSubmit(config: any): void {
    this.isLoading = true;
    this.simulationService.configureSystem(config).subscribe({
      next: () => this.fetchStatus(),
      error: (error) => console.error('Error configuring system:', error),
      complete: () => this.isLoading = false
    });
  }

  private fetchStatus(): void {
    this.simulationService.getSystemStatus().subscribe({
      next: (status) => this.status = status,
      error: (error) => console.error('Error fetching status:', error)
    });
  }

  private startStatusPolling(): void {
    setInterval(() => {
      if (this.isRunning) {
        this.fetchStatus();
      }
    }, 1000);
  }
}
