package com.botreport.botReport.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GeneralAmountReport {
    private String name;
    private BigDecimal tzsAmount;
}
