package com.myEvents.EventTicketingSystem.repository;

import com.myEvents.EventTicketingSystem.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findFirstByEmail(String email);
}
