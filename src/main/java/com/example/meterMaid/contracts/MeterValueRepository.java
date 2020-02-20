package com.example.meterMaid.contracts;

import com.example.meterMaid.Model.MeterValue;

import javax.sql.DataSource;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface MeterValueRepository {
    void setDataSource(DataSource dataSource);
    List<MeterValue> meterValueByCustomerId(String id);
    List<MeterValue> getMeterValuesByMeterDataId(UUID id);
    List<MeterValue> meterValueByDates(Instant from, Instant to);
    Double getMeterValueFromDateToDateByCustomerId(Instant from, Instant to, String id);
    Double getMeterValueFromDateToDateByMeterId(Instant from, Instant to, String id);
    List<MeterValue> saveMeterValues(List<MeterValue> values);
}
