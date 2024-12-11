import { Component, Input } from '@angular/core';
import { SystemStatus } from '../../services/simulation.service';

@Component({
  selector: 'app-status-display',
  templateUrl: './status-display.component.html',
  styleUrls: ['./status-display.component.scss']
})
export class StatusDisplayComponent {
  @Input() status!: SystemStatus;
  @Input() isRunning: boolean = false;
}
