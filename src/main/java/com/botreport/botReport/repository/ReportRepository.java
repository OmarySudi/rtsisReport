package com.botreport.botReport.repository;


import com.botreport.botReport.model.AssetAmountReport;
import com.botreport.botReport.model.GeneralAmountReport;
import com.botreport.botReport.model.LiabilityAmountReport;
import com.botreport.botReport.model.Report;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ReportRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<Report> getServiceReport(String service, String period){

        log.info("ReportRepository::getDailyReport  Querying dailyReport for the {}",service);

        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_getDailyLiabilitySummary")
                .declareParameters(new SqlParameter("table", Types.VARCHAR))
                .returningResultSet("Reports", BeanPropertyRowMapper.newInstance(Report.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("table",service)
                .addValue("period",period);

        Map<String, Object> result = jdbcCall.execute(in);
        log.info("ReportRepository::getDailyReport map result is {}",result);

        List<Report> reports = (List<Report>) result.get("Reports");

        return reports;
    }


    public List<GeneralAmountReport> getGeneralAmountReport(String service){

        log.info("ReportRepository::getGeneralAmountReport  Querying dailyReport for the {}",service);

        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_getDailyTransactionTotal")
                .declareParameters(new SqlParameter("table", Types.VARCHAR))
                .returningResultSet("GeneralAmountReports", BeanPropertyRowMapper.newInstance(GeneralAmountReport.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("table",service);

        Map<String, Object> result = jdbcCall.execute(in);
        log.info("ReportRepository::getGeneralReport map result is {}",result);

        List<GeneralAmountReport> generalAmountReports = (List<GeneralAmountReport>) result.get("GeneralAmountReports");

        return generalAmountReports;
    }


    public List<AssetAmountReport> getAssetAmountReport(String service){

        log.info("ReportRepository::getGeneralReport  Querying dailyReport for the {}",service);

        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_getDailyTransactionTotal")
                .declareParameters(new SqlParameter("table", Types.VARCHAR))
                .returningResultSet("AssetAmountReports", BeanPropertyRowMapper.newInstance(AssetAmountReport.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("table",service);

        Map<String, Object> result = jdbcCall.execute(in);
        log.info("ReportRepository::getAssetAmountReport map result is {}",result);

        List<AssetAmountReport> assetAmountReports = (List<AssetAmountReport>) result.get("AssetAmountReports");

        return assetAmountReports;
    }


    public List<LiabilityAmountReport> getLiabilityAmountReport(String service){

        log.info("ReportRepository::getLiabilityAmountReport  Querying dailyReport for the {}",service);

        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_getDailyTransactionTotal")
                .declareParameters(new SqlParameter("table", Types.VARCHAR))
                .returningResultSet("LiabilityAmountReports", BeanPropertyRowMapper.newInstance(LiabilityAmountReport.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("table",service);

        Map<String, Object> result = jdbcCall.execute(in);
        log.info("ReportRepository::LiabilityAmountReport map result is {}",result);

        List<LiabilityAmountReport> liabilityAmountReports = (List<LiabilityAmountReport>) result.get("LiabilityAmountReports");

        return liabilityAmountReports;
    }
}
