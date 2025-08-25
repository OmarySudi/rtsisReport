package com.botreport.botReport.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class ReportServiceUtil {
    public static String formatNumber(BigDecimal value) {
        if (value == null) {
            return null;
        }
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
        formatter.setGroupingUsed(true);        // enable comma separation
        formatter.setMinimumFractionDigits(2);  // at least 2 decimal places
        formatter.setMaximumFractionDigits(2);  // at most 2 decimal places
        return formatter.format(value);
    }
}
