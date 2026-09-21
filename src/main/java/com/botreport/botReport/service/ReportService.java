package com.botreport.botReport.service;

import com.botreport.botReport.model.*;
import com.botreport.botReport.repository.ReportRepository;
import com.botreport.botReport.util.ReportServiceUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final ReportRepository reportRepository;
    private final JavaMailSender mailSender;
    private final JdbcTemplate jdbcTemplate;
    private String lastToken = "LASTTOKEN";

    @Async
    @Scheduled(cron = "0 00 16 * * *", zone = "Africa/Nairobi")
    public void sendDailyReport(){

        log.info("ReportService::sendDailyReport Execution started ");

        String period = "Daily";

        String[] ccRecipients = {
                "firesoud159@gmail.com"
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
    @Scheduled(cron = "0 02 16 * * *", zone = "Africa/Nairobi")
    public void sendDailyAmountReport(){

        log.info("ReportService::sendDailyAmountReport Execution started ");

        String[] ccRecipients = {
                "firesoud159@gmail.com"
        };

        List<LiabilityAmountReport> liabilityAmountReports = reportRepository.getLiabilityAmountReport("liability");
        List<AssetAmountReport> assetAmountReports = reportRepository.getAssetAmountReport("asset");
        List<GeneralAmountReport> equityAmountReports = reportRepository.getGeneralAmountReport("equity");
        List<GeneralAmountReport> profitAmountReports = reportRepository.getGeneralAmountReport("profitLoss");

        sendAllAmountReports(
                liabilityAmountReports,
                assetAmountReports,
                equityAmountReports,
                profitAmountReports,
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

    private String buildLiabilityAmountHtmlTable(String title, List<LiabilityAmountReport> reports) {

        if (reports == null || reports.isEmpty()) return "";

        StringBuilder builder = new StringBuilder();

        builder.append("<h3>").append(title).append("</h3>");
        builder.append("<table border='1' cellspacing='0' cellpadding='5'>")
                .append("<tr>")
                .append("<th>Name</th>")
                .append("<th>tzsAmount</th>")
                .append("<th>tzsAmountOpening</th>")
                .append("<th>tzsAmountClosing</th>")
                .append("<th>tzsAmountBalance</th>")
                .append("<th>tzsAmountPayment</th>")
                .append("<th>tzsAccruedInterestAmount</th>")
                .append("<th>tzsAmountRepayment</th>")
                .append("<th>tzsAccruedInterestOutstandingAmount</th>")
                .append("<th>tzsInterestAmount</th>")
                .append("<th>tzsTransactionAmount</th>")
                .append("</tr>");

        for (LiabilityAmountReport report : reports) {
            builder.append("<tr>")
                    .append("<td>").append(report.getName()).append("</td>")
                    .append("<td>").append(ReportServiceUtil.formatNumber(report.getTzsAmount())).append("</td>")
                    .append("<td>").append(ReportServiceUtil.formatNumber(report.getTzsAmountOpening())).append("</td>")
                    .append("<td>").append(ReportServiceUtil.formatNumber(report.getTzsAmountClosing())).append("</td>")
                    .append("<td>").append(ReportServiceUtil.formatNumber(report.getTzsAmountBalance())).append("</td>")
                    .append("<td>").append(ReportServiceUtil.formatNumber(report.getTzsAmountPayment())).append("</td>")
                    .append("<td>").append(ReportServiceUtil.formatNumber(report.getTzsAccruedInterestAmount())).append("</td>")
                    .append("<td>").append(ReportServiceUtil.formatNumber(report.getTzsAmountRepayment())).append("</td>")
                    .append("<td>").append(ReportServiceUtil.formatNumber(report.getTzsAccruedInterestOutstandingAmount())).append("</td>")
                    .append("<td>").append(ReportServiceUtil.formatNumber(report.getTzsInterestAmount())).append("</td>")
                    .append("<td>").append(ReportServiceUtil.formatNumber(report.getTzsTransactionAmount())).append("</td>")
                    .append("</tr>");
        }

        builder.append("</table><br/>");
        return builder.toString();
    }


    private String buildAssetAmountHtmlTable(String title, List<AssetAmountReport> reports) {

        if (reports == null || reports.isEmpty()) return "";

        StringBuilder builder = new StringBuilder();

        builder.append("<h3>").append(title).append("</h3>");
        builder.append("<table border='1' cellspacing='0' cellpadding='5'>")
                .append("<tr>")
                .append("<th>Name</th>")
                .append("<th>tzsAmount</th>")
                .append("<th>tzsCostValueAmount</th>")
                .append("<th>tzsLoanAmount</th>")
                .append("<th>tzsTransactionAmount</th>")
                .append("</tr>");

        for (AssetAmountReport report : reports) {
            builder.append("<tr>")
                    .append("<td>").append(report.getName()).append("</td>")
                    .append("<td>").append(ReportServiceUtil.formatNumber(report.getTzsAmount())).append("</td>")
                    .append("<td>").append(ReportServiceUtil.formatNumber(report.getTzsCostValueAmount())).append("</td>")
                    .append("<td>").append(ReportServiceUtil.formatNumber(report.getTzsLoanAmount())).append("</td>")
                    .append("<td>").append(ReportServiceUtil.formatNumber(report.getTzsTransactionAmount())).append("</td>")
                    .append("</tr>");
        }

        builder.append("</table><br/>");
        return builder.toString();
    }


    private String buildGeneralAmountHtmlTable(String title, List<GeneralAmountReport> reports) {

        if (reports == null || reports.isEmpty()) return "";

        StringBuilder builder = new StringBuilder();

        builder.append("<h3>").append(title).append("</h3>");
        builder.append("<table border='1' cellspacing='0' cellpadding='5'>")
                .append("<tr>")
                .append("<th>Name</th>")
                .append("<th>tzsAmount</th>")
                .append("</tr>");

        for (GeneralAmountReport report : reports) {
            builder.append("<tr>")
                    .append("<td>").append(report.getName()).append("</td>")
                    .append("<td>").append(ReportServiceUtil.formatNumber(report.getTzsAmount())).append("</td>")
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


    public void sendAllAmountReports(
            List<LiabilityAmountReport> liabilityAmountReports,
            List<AssetAmountReport> assetAmountReports,
            List<GeneralAmountReport> equityAmountReports,
            List<GeneralAmountReport> profitLossAmountReports,
            String[] ccRecipients
    ){
        String subject = "📊 RTSIS Daily Amount Summary - " + LocalDate.now();

        //Build the HTML content
        StringBuilder emailContent = new StringBuilder();
        emailContent.append(getEmailHeader());
        emailContent.append(buildLiabilityAmountHtmlTable("LIABILITY AMOUNT", liabilityAmountReports));
        emailContent.append(buildAssetAmountHtmlTable("ASSET AMOUNT", assetAmountReports));
        emailContent.append(buildGeneralAmountHtmlTable("EQUITY AMOUNT", equityAmountReports));
        emailContent.append(buildGeneralAmountHtmlTable("PROFIT&LOSS AMOUNT", profitLossAmountReports));
        emailContent.append(getEmailFooter());


        sendEmail(ccRecipients,subject,emailContent);

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

        sendEmail(ccRecipients,subject,emailContent);

    }

    public void sendEmail(
            String[] ccRecipients,
            String subject,
            StringBuilder emailContent
    ){
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

    @Async
    @Scheduled(cron = "0 00 08,12,16 * * *", zone = "Africa/Nairobi")
    public void sendTokenInformationDetailsEmail(){

        String query = "SELECT * FROM bot_services.access_token";
        List<AccessToken> tokenList = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(AccessToken.class));

        String[] recipients = {};
        String subject = "TOKEN INFORMATION";

        if (tokenList.size() > 0) {

            String dbToken = tokenList.get(0).getToken();

            if(dbToken == null){

                if(lastToken != null){ //Connection is lost

                    boolean testConnection = testConnection("196.46.101.97",8245,5000);

                    StringBuilder emailContent = new StringBuilder();

                    String content = "";
                    if(testConnection)
                        content = "New Token from BOT is NULL. Connection is OK";
                     else
                        content = "New Token from BOT is NULL. Connection is LOST";

                    emailContent.append(content);
                    emailContent.append(getEmailFooter());
                    sendEmail(recipients,subject,emailContent);
                }
                lastToken = dbToken;
            } else if(lastToken == null) { //connection is restored


                lastToken = dbToken;

                StringBuilder emailContent = new StringBuilder();
                emailContent.append(getEmailHeader());

                emailContent.append("New TOKEN from BOT is fine now");
                emailContent.append(getEmailFooter());
                sendEmail(recipients,subject,emailContent);
            }

        }
    }

    public boolean testConnection(String host, int port, int timeout){
        try(Socket socket = new Socket()){

            socket.connect(
                    new InetSocketAddress(host,port),
                    timeout
            );

            return true;

        } catch(IOException e){

            log.info("FAIL - Cannot connect to {} : {}",host,port);
            log.info("Reason: {}",e.getMessage());

            return false;
        }
    }

}
