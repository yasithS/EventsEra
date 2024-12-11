package com.myEvents.EventTicketingSystem.services.authentication;

import com.myEvents.EventTicketingSystem.dto.CustomerDto;
import com.myEvents.EventTicketingSystem.dto.SignupRequestDTO;

public interface AuthService {
    CustomerDto signupClient(SignupRequestDTO signupRequestDTO);

    Boolean presentByEmail(String email);

    CustomerDto signupVendor(SignupRequestDTO signupRequestDTO);
}
