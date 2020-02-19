package com.example.meterMaid.dao;

import com.example.meterMaid.Model.MeterValue;
import com.example.meterMaid.contracts.MeterValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class MeterValueRepositoryImpl implements MeterValueRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<MeterValue> meterValueByCustomerId(String id) {
        String sql = "select * from metervalues where userid = ?";
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
        Timestamp fromtime =java.sql.Timestamp.from(from);
        Timestamp totime =java.sql.Timestamp.from(to);
        String sql = "select * from metervalues where hour >= ? and hour <= ?";
        return  jdbcTemplate.query(sql, new Object[]{fromtime, totime},((rs, rowNum) -> new MeterValue(
                (UUID) rs.getObject("id"),
                (UUID) rs.getObject("meterdataid"),
                rs.getString("meterid"),
                rs.getString("userid"),
                rs.getTimestamp("hour").toInstant(),
                rs.getDouble("value")
        )));
    }

    @Override
    public List<MeterValue> getMeterValueFromDateToDateByCustomerId(Instant from, Instant to, String id) {
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
    public List<MeterValue> saveMeterValues(List<MeterValue> values) {
        List<Object[]> batch = new ArrayList<Object[]>();
        for (MeterValue value: values){
            Object[] valuesForInsertion = new Object[]{
              value.getId(),
              value.getMeterDataId(),
              value.getMeterId(),
              value.getCustomerId(),
              java.sql.Timestamp.from(value.getHour()),
              value.getValue()
            };
            batch.add(valuesForInsertion);
        }
        jdbcTemplate.batchUpdate(
                "insert into metervalues(id, meterdataid, meterid, userid, hour, value) values(?,?,?,?,?,?)",
                batch);

        String newSql = "select * from metervalues where meterdataid = ?";
        List<MeterValue> returnedMeterValues = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(newSql, values.get(0).getMeterDataId());
        for (Map row : rows) {
            MeterValue savedMeterValue = new MeterValue();
            savedMeterValue.setId(((UUID) row.get("id")));
            savedMeterValue.setMeterDataId((UUID) row.get("meterdataid"));
            savedMeterValue.setMeterId((String) row.get("meterid"));
            savedMeterValue.setCustomerId((String) row.get("userid"));
            Timestamp hour = (Timestamp) row.get("hour");
            savedMeterValue.setHour(hour.toInstant());
            savedMeterValue.setValue((Double) row.get("value"));
            returnedMeterValues.add(savedMeterValue);
        }
        return returnedMeterValues;
    }
}
