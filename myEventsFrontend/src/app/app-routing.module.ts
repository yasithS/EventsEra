import { NgModule, Component } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SignupCustomerComponent } from './basic/components/signup-customer/signup-customer.component';

const routes: Routes = [
  { path: 'register_customer', component: SignupCustomerComponent },
  { path: 'vendor', loadChildren: () => import('./vendor/vendor.module').then(m => m.VendorModule) }, 
  { path: 'customer', loadChildren: () => import('./customer/customer.module').then(m => m.CustomerModule) }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
