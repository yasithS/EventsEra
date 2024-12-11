import { Component, EventEmitter, Output, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SimulationConfig } from '../../services/simulation.service';

@Component({
  selector: 'app-config-form',
  templateUrl: './config-form.component.html',
  styleUrls: ['./config-form.component.scss']
})
export class ConfigFormComponent {
  @Input() isRunning: boolean = false;
  @Output() configSubmit = new EventEmitter<SimulationConfig>();
  
  configForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.configForm = this.fb.group({
      maxTicketPool: [100, [Validators.required, Validators.min(1)]],
      totalVendors: [5, [Validators.required, Validators.min(1)]],
      releasePerVendor: [20, [Validators.required, Validators.min(1)]],
      vendorReleaseRate: [2, [Validators.required, Validators.min(1)]],
      totalCustomers: [10, [Validators.required, Validators.min(1)]],
      ticketsPerCustomer: [2, [Validators.required, Validators.min(1)]],
      customerBuyingRate: [3, [Validators.required, Validators.min(1)]]
    });
  }

  onSubmit() {
    if (this.configForm.valid) {
      this.configSubmit.emit(this.configForm.value);
    }
  }
}