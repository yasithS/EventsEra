package com.myEvents.EventTicketingSystem.controller;

import com.myEvents.EventTicketingSystem.dto.CustomerDto;
import com.myEvents.EventTicketingSystem.dto.SignupRequestDTO;
import com.myEvents.EventTicketingSystem.services.authentication.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthService authService;

    @PostMapping("/customer/sign-up")
    public ResponseEntity<?> signupCustomer(@RequestBody SignupRequestDTO signupRequestDTO) {

        if(authService.presentByEmail(signupRequestDTO.getEmail())) {
            return new ResponseEntity<>("Customer already exists", HttpStatus.NOT_ACCEPTABLE);
        }

        CustomerDto createdCustomer = authService.signupClient(signupRequestDTO);
        return new ResponseEntity<>(createdCustomer, HttpStatus.OK);


    }

    @PostMapping("/vendor/sign-up")
    public ResponseEntity<?> signupVendor(@RequestBody SignupRequestDTO signupRequestDTO) {

        if(authService.presentByEmail(signupRequestDTO.getEmail())) {
            return new ResponseEntity<>("Vendor already exists", HttpStatus.NOT_ACCEPTABLE);
        }

        CustomerDto createdVendor = authService.signupClient(signupRequestDTO);
        return new ResponseEntity<>(createdVendor, HttpStatus.OK);


    }


}
