package com.botreport.botReport.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LiabilityAmountReport {
    private String name;
    private BigDecimal tzsAmount;
    private BigDecimal tzsAmountOpening;
    private BigDecimal tzsAmountClosing;
    private BigDecimal tzsAmountBalance;
    private BigDecimal tzsAmountPayment;
    private BigDecimal tzsAccruedInterestAmount;
    private BigDecimal tzsAmountRepayment;
    private BigDecimal tzsAccruedInterestOutstandingAmount;
    private BigDecimal tzsInterestAmount;
    private BigDecimal tzsTransactionAmount;
}
