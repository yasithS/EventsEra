package com.myEvents.Backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ThreadLogDTO {
    private Long id;
    private String threadType;
    private String action;
    private LocalDateTime timestamp;
    private String message;
}