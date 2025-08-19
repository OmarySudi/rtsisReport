package com.botreport.botReport.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Report {
    private String name;
    private String processingStatus;
    private int total;
    private LocalDate sentDate;
}
