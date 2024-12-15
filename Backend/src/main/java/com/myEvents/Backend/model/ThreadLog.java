
package com.myEvents.Backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class ThreadLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String threadType;
    private String action;
    private LocalDateTime timestamp;
    private String message;
}