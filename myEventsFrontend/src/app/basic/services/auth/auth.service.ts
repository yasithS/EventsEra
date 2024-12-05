import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

const BASIC_URL = 'http://localhost:8080';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient,) { }

  registerCustomer(signupRequestDTO:any):Observable<any>{
    return this.http.post(BASIC_URL + "customer/sign-up", signupRequestDTO);
  }

  registerVendor(signupRequestDTO:any):Observable<any>{
    return this.http.post(BASIC_URL + "company/sign-up", signupRequestDTO);
  }
}
