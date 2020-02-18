package com.example.meterMaid.contracts;

import com.example.meterMaid.Model.MeterData;

import javax.sql.DataSource;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface MeterDataRepository {
    void setDataSource(DataSource dataSource);
    List<MeterData> getAll();
    List<MeterData> getMeterDataFromDateToDate(Instant from, Instant to);
    MeterData getMeterDataById(UUID id);
    MeterData saveMeterData(MeterData meterData);
    List<MeterData> getMeterDataFromDateToDateByMeterId(Instant from, Instant to, String id);
    List<MeterData> getMeterDataFromDateToDateByCustomerId(Instant from, Instant to, String id);
}
