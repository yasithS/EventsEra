import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TicketDashboardComponent } from './components/ticket-dashboard/ticket-dashboard.component';
import { ConfigFormComponent } from './components/config-form/config-form.component';
import { StatusDisplayComponent } from './components/status-display/status-display.component';
import { SimulationService } from './services/simulation.service';

@NgModule({
  declarations: [
    AppComponent,
    TicketDashboardComponent,
    ConfigFormComponent,
    StatusDisplayComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule
  ],
  providers: [SimulationService],
  bootstrap: [AppComponent]
})
export class AppModule { }