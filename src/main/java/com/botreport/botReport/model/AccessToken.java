package com.botreport.botReport.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public final class AccessToken {
    @JsonIgnore
    private Long id;
    private String token;
    private LocalDateTime expiresAt;
}
