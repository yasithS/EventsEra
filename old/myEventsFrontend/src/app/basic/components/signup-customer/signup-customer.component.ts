import { AuthService } from './../../services/auth/auth.service';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { error } from 'console';
import { Router } from '@angular/router';
import { NzNotificationService } from 'ng-zorro-antd/notification';

@Component({
  selector: 'app-signup-customer',
  templateUrl: './signup-customer.component.html',
  styleUrl: './signup-customer.component.css'
})
export class SignupCustomerComponent {

  validateForm! : FormGroup;

  constructor (private fb: FormBuilder,
    private AuthService: AuthService,
    private notification: NzNotificationService,
    private router: Router){}

    ngOnInit(){
      this.validateForm = this.fb.group({
        email: [null, [Validators.email, Validators.required]],
        firstname:[null, [Validators.required]],
        lastname:[null, [Validators.required]], 
        phone : [null],
        password:[null, [Validators.required]],
        checkpassword:[null, [Validators.required]],
      })
    }
    submitForm(){
      this.AuthService.registerCustomer(this.validateForm.value).subscribe(res =>{

        this.notification
        .success(
          'SUCCESS',
          `Signup Successful`,
          { nzDuration:5000 }
        );
        this.router.navigateByUrl('/login');
      }, error =>{
        this.notification
        .error(
          'ERROR',
          `${error.error}`,
          { nzDuration: 5000 }
        )

      });
    }

}
