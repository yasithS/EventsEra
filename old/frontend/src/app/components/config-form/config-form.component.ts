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
      maxTicketPool: [0, [Validators.required, Validators.min(1)]],
      totalVendors: [0, [Validators.required, Validators.min(1)]],
      releasePerVendor: [0, [Validators.required, Validators.min(1)]],
      vendorReleaseRate: [0, [Validators.required, Validators.min(1)]],
      totalCustomers: [0, [Validators.required, Validators.min(1)]],
      ticketsPerCustomer: [0, [Validators.required, Validators.min(1)]],
      customerBuyingRate: [0, [Validators.required, Validators.min(1)]]
    });
  }

  onSubmit() {
    if (this.configForm.valid) {
      this.configSubmit.emit(this.configForm.value);
    }
  }
}