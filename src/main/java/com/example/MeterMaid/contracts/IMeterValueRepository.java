package com.example.MeterMaid.contracts;

import com.example.MeterMaid.Model.MeterValue;

import javax.sql.DataSource;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface IMeterValueRepository {
    void setDataSource(DataSource dataSource);
    List<MeterValue> meterValueByCustomerId(String id);
    List<MeterValue> getMeterValuesByMeterDataId(UUID id);
    List<MeterValue> meterValueByDates(Instant from, Instant to);
    List<MeterValue> getMeterValueFromDateToDateByUserId(Instant from, Instant to, String id);
    List<MeterValue> getMeterValueFromDateToDateByMeterId(Instant from, Instant to, String id);
    MeterValue saveMeterValue(MeterValue value);
}
