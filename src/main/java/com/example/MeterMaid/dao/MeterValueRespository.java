package com.example.MeterMaid.dao;

import com.example.MeterMaid.Model.MeterValue;
import com.example.MeterMaid.contracts.IMeterValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public class MeterValueRespository implements IMeterValueRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<MeterValue> meterValueByCustomerId(String id) {
        String sql = "select * from metervalues where customerid = ?";
        return  jdbcTemplate.query(sql, new Object[]{id},((rs, rowNum) -> new MeterValue(
                (UUID) rs.getObject("id"),
                (UUID) rs.getObject("meterdataid"),
                rs.getString("meterid"),
                rs.getString("userid"),
                rs.getTimestamp("hour").toInstant(),
                rs.getDouble("value")
        )));
    }

    @Override
    public List<MeterValue> getMeterValuesByMeterDataId(UUID id) {
        String sql = "select * from metervalues where meterdataid = ?";
        return  jdbcTemplate.query(sql, new Object[]{id},((rs, rowNum) -> new MeterValue(
                (UUID) rs.getObject("id"),
                (UUID) rs.getObject("meterdataid"),
                rs.getString("meterid"),
                rs.getString("userid"),
                rs.getTimestamp("hour").toInstant(),
                rs.getDouble("value")
        )));
    }

    @Override
    public List<MeterValue> meterValueByDates(Instant from, Instant to) {
        String sql = "select * from metervalues where hour >= ? and hour <= ?";
        return  jdbcTemplate.query(sql, new Object[]{from, to},((rs, rowNum) -> new MeterValue(
                (UUID) rs.getObject("id"),
                (UUID) rs.getObject("meterdataid"),
                rs.getString("meterid"),
                rs.getString("userid"),
                rs.getTimestamp("hour").toInstant(),
                rs.getDouble("value")
        )));
    }

    @Override
    public List<MeterValue> getMeterValueFromDateToDateByUserId(Instant from, Instant to, String id) {
        Timestamp fromtime =java.sql.Timestamp.from(from);
        Timestamp totime =java.sql.Timestamp.from(to);
        String sql = "select * from metervalues where hour >= ? and hour <= ? and userid = ?";
        return  jdbcTemplate.query(sql, new Object[]{fromtime, totime, id},((rs, rowNum) -> new MeterValue(
                (UUID) rs.getObject("id"),
                (UUID) rs.getObject("meterdataid"),
                rs.getString("meterid"),
                rs.getString("userid"),
                rs.getTimestamp("hour").toInstant(),
                rs.getDouble("value")
        )));
    }

    @Override
    public List<MeterValue> getMeterValueFromDateToDateByMeterId(Instant from, Instant to, String id) {
        Timestamp fromtime =java.sql.Timestamp.from(from);
        Timestamp totime =java.sql.Timestamp.from(to);
        String sql = "select * from metervalues where hour >= ? and hour <= ? and meterid = ?";
        return  jdbcTemplate.query(sql, new Object[]{fromtime, totime, id},((rs, rowNum) -> new MeterValue(
                (UUID) rs.getObject("id"),
                (UUID) rs.getObject("meterdataid"),
                rs.getString("meterid"),
                rs.getString("userid"),
                rs.getTimestamp("hour").toInstant(),
                rs.getDouble("value")
        )));
    }

    @Override
    public MeterValue saveMeterValue(MeterValue value) {
        Timestamp hour =java.sql.Timestamp.from(value.getHour());
        jdbcTemplate.update(
                "insert into metervalues(id, meterdataid, meterid, userid, hour, value) values(?,?,?,?,?,?)",
                value.getId(), value.getMeterDataId(), value.getMeterId(), value.getUserId(),hour, value.getValue());

        String newSql = "select * from metervalues where id = ? limit 1";
        return jdbcTemplate.queryForObject(newSql, new Object[]{value.getId()}, ((rs, rowNum) ->
                new MeterValue(
                        (UUID) rs.getObject("id"),
                        (UUID) rs.getObject("meterdataid"),
                        rs.getString("meterid"),
                        rs.getString("userid"),
                        rs.getTimestamp("hour").toInstant(),
                        rs.getDouble("value")
                )
        ));
    }
}
