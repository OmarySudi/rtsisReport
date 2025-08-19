package com.botreport.botReport.repository;


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
}
