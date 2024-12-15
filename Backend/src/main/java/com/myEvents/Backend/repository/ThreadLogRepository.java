// src/main/java/com/myEvents/Backend/repository/ThreadLogRepository.java
package com.myEvents.Backend.repository;

import com.myEvents.Backend.model.ThreadLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ThreadLogRepository extends JpaRepository<ThreadLog, Long> {
    List<ThreadLog> findTop50ByOrderByTimestampDesc();
}