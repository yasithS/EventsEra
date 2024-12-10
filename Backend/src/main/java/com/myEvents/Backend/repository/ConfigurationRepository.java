package com.myEvents.Backend.repository;

import com.myEvents.Backend.model.SystemConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepository extends JpaRepository<SystemConfiguration, Long> {
    SystemConfiguration findTopByOrderByIdDesc();
}
