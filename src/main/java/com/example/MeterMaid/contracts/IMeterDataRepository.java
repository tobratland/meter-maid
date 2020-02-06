package com.example.MeterMaid.contracts;

import com.example.MeterMaid.Model.MeterData;

import javax.sql.DataSource;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface IMeterDataRepository {
    void setDataSource(DataSource dataSource);
    List<MeterData> GetAll();
    List<MeterData> GetMeterDataFromDateToDate(Instant from, Instant to);
    MeterData GetMeterDataById(UUID id);
    MeterData SaveMeterData(MeterData meterData);
    List<MeterData> getMeterDataFromDateToDateByMeterId(Instant from, Instant to, String id);
    List<MeterData> getMeterDataFromDateToDateByCustomerId(Instant from, Instant to, String id);
}
