package com.botreport.botReport.service;

import com.botreport.botReport.model.Report;
import com.botreport.botReport.repository.ReportRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final ReportRepository reportRepository;
    private final JavaMailSender mailSender;

    @Async
    @Scheduled(cron = "0 58 23 * * *", zone = "Africa/Nairobi")
    public void sendDailyReport(){

        log.info("ReportService::sendDailyReport Execution started ");

        String period = "Daily";

        String[] ccRecipients = {};

        List<Report> liabilityReports = reportRepository.getServiceReport("liability",period);
        List<Report> assetReports = reportRepository.getServiceReport("asset",period);
        List<Report> equityReports = reportRepository.getServiceReport("equity",period);
        List<Report> othersReports = reportRepository.getServiceReport("others",period);
        List<Report> profitReports = reportRepository.getServiceReport("profitLoss",period);

        sendAllReports(
                liabilityReports,
                assetReports,
                equityReports,
                othersReports,
                profitReports,
                period,
                ccRecipients
        );

    }


    @Async
    @Scheduled(cron = "0 55 23 * * SUN", zone = "Africa/Nairobi")
    public void sendWeeklyReport(){

        log.info("ReportService::sendWeeklyReport Execution started ");

        String period = "Weekly";

        String[] ccRecipients = {
                //"firesoud159@gmail.com"
        };

        List<Report> liabilityReports = reportRepository.getServiceReport("liability",period);
        List<Report> assetReports = reportRepository.getServiceReport("asset",period);
        List<Report> equityReports = reportRepository.getServiceReport("equity",period);
        List<Report> othersReports = reportRepository.getServiceReport("others",period);
        List<Report> profitReports = reportRepository.getServiceReport("profitLoss",period);

        sendAllReports(
                liabilityReports,
                assetReports,
                equityReports,
                othersReports,
                profitReports,
                period,
                ccRecipients
        );

    }


    @Async
    @Scheduled(cron = "0 50 23 * * *", zone = "Africa/Nairobi")
    public void sendMonthlyReport(){

        LocalDate today = LocalDate.now(ZoneId.of("Africa/Nairobi"));

        if (today.getDayOfMonth() == today.lengthOfMonth()) {

            log.info("ReportService::sendMonthlyReport Execution started ");

            String period = "Monthly";

            String[] ccRecipients = {
                    //"firesoud159@gmail.com"
            };

            List<Report> liabilityReports = reportRepository.getServiceReport("liability",period);
            List<Report> assetReports = reportRepository.getServiceReport("asset",period);
            List<Report> equityReports = reportRepository.getServiceReport("equity",period);
            List<Report> othersReports = reportRepository.getServiceReport("others",period);
            List<Report> profitReports = reportRepository.getServiceReport("profitLoss",period);

            sendAllReports(
                    liabilityReports,
                    assetReports,
                    equityReports,
                    othersReports,
                    profitReports,
                    period,
                    ccRecipients
            );
        }

    }

    private String buildHtmlTable(String title, List<Report> reports) {

        if (reports == null || reports.isEmpty()) return "";

        StringBuilder builder = new StringBuilder();

        builder.append("<h3>").append(title).append("</h3>");
        builder.append("<table border='1' cellspacing='0' cellpadding='5'>")
                .append("<tr>")
                .append("<th>Name</th>")
                .append("<th>Status</th>")
                .append("<th>Total</th>")
                .append("</tr>");

        for (Report report : reports) {
            builder.append("<tr>")
                    .append("<td>").append(report.getName()).append("</td>")
                    .append("<td>").append(report.getProcessingStatus()).append("</td>")
                    .append("<td>").append(report.getTotal()).append("</td>")
                    .append("</tr>");
        }

        builder.append("</table><br/>");
        return builder.toString();
    }


    private String getEmailHeader() {
        return """
            <html>
            <head>
              <style>
                table {
                    border-collapse: collapse;
                    width: 100%;
                    margin-bottom: 20px;
                }
                th, td {
                    border: 1px solid #ddd;
                    padding: 8px;
                    text-align: left;
                }
                th {
                    background-color: #f2f2f2;
                    color: #333;
                }
                h3 {
                    color: #004085;
                }
              </style>
            </head>
            <body>
            <h2>📅 Summary - """ + LocalDate.now() + "</h2>";
    }

    private String getEmailFooter() {
        return """
            <p>--<br/>
            This is an automated message from rtsis system.</p>
            </body>
            </html>
            """;
    }


    public void sendAllReports(
            List<Report> liabilities,
            List<Report> assets,
            List<Report> equities,
            List<Report> bank0thers,
            List<Report> profitLoss,
            String period,
            String[] ccRecipients
    ) {
        String subject = "📊 RTSIS "+period+" Summary - " + LocalDate.now();


        // Build the HTML content
        StringBuilder emailContent = new StringBuilder();
        emailContent.append(getEmailHeader());
        emailContent.append(buildHtmlTable("LIABILITIES", liabilities));
        emailContent.append(buildHtmlTable("ASSETS", assets));
        emailContent.append(buildHtmlTable("EQUITY", equities));
        emailContent.append(buildHtmlTable("BANK OTHERS", bank0thers));
        emailContent.append(buildHtmlTable("PROFIT&LOSS", profitLoss));
        emailContent.append(getEmailFooter());

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo("kekovasudi@gmail.com");// Change to actual
            helper.setCc(ccRecipients);
            helper.setSubject(subject);
            helper.setText(emailContent.toString(), true); // true = HTML

            mailSender.send(message);
            System.out.println("✅ Reports email sent successfully.");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
