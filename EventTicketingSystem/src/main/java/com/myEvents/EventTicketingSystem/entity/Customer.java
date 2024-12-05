package com.myEvents.EventTicketingSystem.entity;


import com.myEvents.EventTicketingSystem.dto.CustomerDto;
import com.myEvents.EventTicketingSystem.enums.CustomerRole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="customers")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private CustomerRole role;


    public CustomerDto getDto(){

        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(id);
        customerDto.setFirstName(firstName);
        customerDto.setEmail(email);
        customerDto.setRole(role);

        return customerDto;

    }












}
