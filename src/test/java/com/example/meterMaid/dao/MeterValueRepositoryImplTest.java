package com.example.meterMaid.dao;

import com.example.meterMaid.Model.MeterValue;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class MeterValueRepositoryImplTest {

    @Autowired
    private MeterValueRepositoryImpl meterValueRepositoryImpl;

    @BeforeEach
    @Transactional
    @Rollback(true)
    void setUp(){
        List<MeterValue> values = new ArrayList<>();
        values.add(meterValue1);
        values.add(meterValue2);
        values.add(meterValue3);

        meterValueRepositoryImpl.saveMeterValues(values);
    }

    @Test
    void meterValueByCustomerId() {
        List<MeterValue> meterValueList =  meterValueRepositoryImpl.meterValueByCustomerId("testing123");
        Assert.assertEquals(meterValueList.size(), 3);
    }

    @Test
    void getMeterValuesByMeterDataId() {
        List<MeterValue> meterValueList = meterValueRepositoryImpl.getMeterValuesByMeterDataId(UUID.fromString("7c6d61a3-faa4-466e-82ed-a6f21df31878"));
        Assert.assertEquals(meterValueList.size(), 2);
        Assert.assertTrue(meterValueList.get(1).getMeterId().equals(meterValue1.getMeterId()) || meterValueList.get(1).getMeterId().equals(meterValue2.getMeterId()));
    }

    @Test
    void meterValueByDates() {
        List<MeterValue> meterValuesList = meterValueRepositoryImpl.meterValueByDates(Instant.parse("1900-08-09T00:00:00Z"), Instant.parse("1900-08-11T00:00:00Z"));
        Assert.assertEquals(meterValuesList.size(), 3);
        Assert.assertTrue(meterValuesList.get(1).getMeterId().equals(meterValue1.getMeterId()) || meterValuesList.get(1).getMeterId().equals(meterValue2.getMeterId()) || meterValuesList.get(1).getMeterId().equals(meterValue2.getMeterId()));

    }

    @Test
    void getMeterValueFromDateToDateByCustomerId() {
        Double returnedValue = meterValueRepositoryImpl.getMeterValueFromDateToDateByCustomerId(Instant.parse("1900-08-11T00:00:00Z"),Instant.parse("1900-08-11T00:01:00Z"),"testing123");
        Assert.assertNotNull(returnedValue);
        Assert.assertEquals(returnedValue, meterValue3.getValue(),0.01);

    }

    @Test
    void getMeterValueFromDateToDateByMeterId() {
        Double returnedValue = meterValueRepositoryImpl.getMeterValueFromDateToDateByMeterId(Instant.parse("1900-08-11T00:00:00Z"),Instant.parse("1900-08-11T00:01:00Z"),"test234");
        Assert.assertNotNull(returnedValue);
        Assert.assertEquals(returnedValue, meterValue3.getValue(),0.01);
    }

    @Test
    void saveMeterValue() {
        List<MeterValue> values = new ArrayList<>();
        values.add(meterValue4);
        List<MeterValue> meterValues = meterValueRepositoryImpl.saveMeterValues(values);
        Assert.assertEquals(meterValues.get(0).getId(), meterValue4.getId());
        Assert.assertEquals(meterValues.get(0).getCustomerId(), meterValue4.getCustomerId());
        Assert.assertEquals(meterValues.get(0).getValue(), meterValue4.getValue(), 0.01);
    }
    //UUID id, UUID meterDataId, String meterId, String userId, Instant hour, double value
    private MeterValue meterValue1 = new MeterValue(
        UUID.fromString("7c6d61a3-faa4-466e-82ed-a6f21df31878"),
        UUID.fromString("7c6d61a3-faa4-466e-82ed-a6f21df31878"),
        "test123",
        "testing123",
        Instant.parse("1900-08-09T00:00:00Z"),
        22.2
    );
    private MeterValue meterValue2 = new MeterValue(
            UUID.fromString("7c6d61a3-faa4-477e-82ed-a6f21df31878"),
            UUID.fromString("7c6d61a3-faa4-466e-82ed-a6f21df31878"),
            "test123",
            "testing123",
            Instant.parse("1900-08-10T00:00:00Z"),
            22.2
    );
    private MeterValue meterValue3 = new MeterValue(
            UUID.fromString("7c6d61a3-faa4-488e-82ed-a6f21df31878"),
            UUID.fromString("7c6d61a3-faa4-466e-82ed-a6f21df31889"),
            "test234",
            "testing123",
            Instant.parse("1900-08-11T00:00:00Z"),
            22.2
    );
    private MeterValue meterValue4 = new MeterValue(
            UUID.fromString("7c6d61a3-faa4-499e-82ed-a6f21df31988"),
            UUID.fromString("7c6d61a3-faa4-466e-82ed-a6f22df31878"),
            "test456",
            "testing234",
            Instant.parse("1900-08-12T00:00:00Z"),
            22.2
    );

}