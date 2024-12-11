package com.myEvents.EventTicketingSystem.dto;

import com.myEvents.EventTicketingSystem.enums.CustomerRole;
import lombok.Data;

@Data
public class CustomerDto {
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private CustomerRole role;



}
