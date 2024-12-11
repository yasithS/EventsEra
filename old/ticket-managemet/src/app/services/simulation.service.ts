import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SimulationConfig {
  maxTicketPool: number;
  totalVendors: number;
  releasePerVendor: number;
  vendorReleaseRate: number;
  totalCustomers: number;
  ticketsPerCustomer: number;
  customerBuyingRate: number;
}

export interface SystemStatus {
  maximumPoolSize: number;
  currentPoolSize: number;
  availableSpace: number;
  totalTicketsReleased: number;
  totalTicketsBought: number;
  activeVendorThreads: number;
  activeCustomerThreads: number;
}

@Injectable({
  providedIn: 'root'
})
export class SimulationService {
  private apiUrl = 'api/simulation';

  constructor(private http: HttpClient) { }

  configureSystem(config: SimulationConfig): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/configure`, config);
  }

  startSimulation(): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/start`, {});
  }

  stopSimulation(): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/stop`, {});
  }

  getSystemStatus(): Observable<SystemStatus> {
    return this.http.get<SystemStatus>(`${this.apiUrl}/status`);
  }
}