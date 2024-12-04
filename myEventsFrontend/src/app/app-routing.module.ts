import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [{ path: 'vendor', loadChildren: () => import('./vendor/vendor.module').then(m => m.VendorModule) }, { path: 'customer', loadChildren: () => import('./customer/customer.module').then(m => m.CustomerModule) }];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
