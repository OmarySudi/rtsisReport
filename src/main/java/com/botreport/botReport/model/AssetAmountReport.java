package com.botreport.botReport.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AssetAmountReport {
    private String name;
    private BigDecimal tzsAmount;
    private BigDecimal tzsCostValueAmount;
    private BigDecimal tzsLoanAmount;
    private BigDecimal tzsTransactionAmount;
}
