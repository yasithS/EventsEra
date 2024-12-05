package com.myEvents.EventTicketingSystem.dto;

import lombok.Data;

@Data
public class SignupRequestDTO {

    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;



}
