package com.example.MeterMaid.dao;

import com.example.MeterMaid.Model.MeterData;
import com.example.MeterMaid.contracts.IMeterDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public class MeterDataRepository implements IMeterDataRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    @Override
    public List<MeterData> GetAll() {
        String sql = "select * from meterdata";
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new MeterData(
                        (UUID) rs.getObject("id"),
                        rs.getString("meterid"),
                        rs.getString("userid"),
                        rs.getString("resolution"),
                        rs.getTimestamp("fromtime").toInstant(),
                        rs.getTimestamp("totime").toInstant()
                )
        );
    }

    @Override
    public List<MeterData> GetMeterDataFromDateToDate(Instant from, Instant to) {
        Timestamp fromtime =java.sql.Timestamp.from(from);
        Timestamp totime =java.sql.Timestamp.from(to);
        String sql = "select * from meterdata where fromtime >= ? and totime <= ?";
        return jdbcTemplate.query(sql, new Object[]{fromtime, totime}, ((rs, rowNum) -> new MeterData(
                (UUID) rs.getObject("id"),
                rs.getString("meterid"),
                rs.getString("userid"),
                rs.getString("resolution"),
                rs.getTimestamp("fromtime").toInstant(),
                rs.getTimestamp("totime").toInstant()
        )));
    }

    @Override
    public MeterData GetMeterDataById(UUID id) {
        String sql = "select * from meterData where id = ? limit 1";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, ((rs, rowNum) ->
                new MeterData(
                        (UUID) rs.getObject("id"),
                        rs.getString("meterid"),
                        rs.getString("userid"),
                        rs.getString("resolution"),
                        rs.getTimestamp("fromtime").toInstant(),
                        rs.getTimestamp("totime").toInstant()
                )
        ));
    }

    @Override
    public MeterData SaveMeterData(MeterData meterData) {
        Timestamp from =java.sql.Timestamp.from(meterData.getFrom());
        Timestamp to =java.sql.Timestamp.from(meterData.getTo());
        jdbcTemplate.update(
        "insert into meterdata(id, meterid, userid, resolution, fromtime, totime) values(?,?,?,?,?,?)",
                meterData.getId(), meterData.getMeter_id(), meterData.getCustomer_id(), meterData.getResolution(), from, to);

        String newSql = "select * from meterdata where id = ?";
        return jdbcTemplate.queryForObject(newSql, new Object[]{meterData.getId()}, ((rs, rowNum) ->
                new MeterData(
                        (UUID) rs.getObject("id"),
                        rs.getString("meterid"),
                        rs.getString("userid"),
                        rs.getString("resolution"),
                        rs.getTimestamp("fromtime").toInstant(),
                        rs.getTimestamp("totime").toInstant()

                )
        ));
    }

    @Override
    public List<MeterData> getMeterDataFromDateToDateByMeterId(Instant from, Instant to, String id) {
        Timestamp fromtime =java.sql.Timestamp.from(from);
        Timestamp totime =java.sql.Timestamp.from(to);
        String sql = "select * from meterdata where fromtime >= ? and totime <= ? and meterid = ?";
        return jdbcTemplate.query(sql, new Object[]{fromtime, totime, id}, ((rs, rowNum) -> new MeterData(
                (UUID) rs.getObject("id"),
                rs.getString("meterid"),
                rs.getString("userid"),
                rs.getString("resolution"),
                rs.getTimestamp("fromtime").toInstant(),
                rs.getTimestamp("totime").toInstant()
        )));
    }

    @Override
    public List<MeterData> getMeterDataFromDateToDateByCustomerId(Instant from, Instant to, String id) {
        Timestamp fromtime =java.sql.Timestamp.from(from);
        Timestamp totime =java.sql.Timestamp.from(to);
        String sql = "select * from meterdata where fromtime >= ? and totime <= ? and userid = ?";
        return jdbcTemplate.query(sql, new Object[]{fromtime, totime, id}, ((rs, rowNum) -> new MeterData(
                (UUID) rs.getObject("id"),
                rs.getString("meterid"),
                rs.getString("userid"),
                rs.getString("resolution"),
                rs.getTimestamp("fromtime").toInstant(),
                rs.getTimestamp("totime").toInstant()
        )));
    }
}
