package com.myEvents.EventTicketingSystem.services.authentication;

import com.myEvents.EventTicketingSystem.dto.CustomerDto;
import com.myEvents.EventTicketingSystem.dto.SignupRequestDTO;
import com.myEvents.EventTicketingSystem.entity.Customer;
import com.myEvents.EventTicketingSystem.enums.CustomerRole;
import com.myEvents.EventTicketingSystem.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private CustomerRepository customerRepository;

    public CustomerDto signupClient(SignupRequestDTO signupRequestDTO){

        Customer customer = new Customer();

        customer.setFirstName(signupRequestDTO.getFirstName());
        customer.setLastName(signupRequestDTO.getLastName());
        customer.setEmail(signupRequestDTO.getEmail());
        customer.setPhone(signupRequestDTO.getPhone());
        customer.setPassword(signupRequestDTO.getPassword());

        customer.setRole(CustomerRole.CUSTOMER);

        return customerRepository.save(customer).getDto();

    }

    public Boolean presentByEmail(String email){
        return customerRepository.findFirstByEmail(email) != null;
    }


    public CustomerDto signupVendor(SignupRequestDTO signupRequestDTO){

        Customer customer = new Customer();

        customer.setFirstName(signupRequestDTO.getFirstName());
        customer.setEmail(signupRequestDTO.getEmail());
        customer.setPhone(signupRequestDTO.getPhone());
        customer.setPassword(signupRequestDTO.getPassword());

        customer.setRole(CustomerRole.VENDOR);

        return customerRepository.save(customer).getDto();

    }

}
