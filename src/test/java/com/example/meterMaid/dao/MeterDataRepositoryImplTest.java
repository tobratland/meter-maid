package com.example.meterMaid.dao;

import com.example.meterMaid.Model.MeterData;
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
import java.util.List;
import java.util.UUID;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class MeterDataRepositoryImplTest {

    @Autowired
    private MeterDataRepositoryImpl meterDataRepositoryImpl;

    @BeforeEach
    @Transactional
    @Rollback(true)
    void setUp(){
        meterDataRepositoryImpl.saveMeterData(meterData1);
        meterDataRepositoryImpl.saveMeterData(meterData2);
    }

    @Test
    void getAll() throws Exception {
        List<MeterData> meterDataList = meterDataRepositoryImpl.getAll();
        Assert.assertEquals(meterDataList.size(), 2);
    }

    @Test
    void getMeterDataFromDateToDate() throws Exception {
        List<MeterData> meterDataList =  meterDataRepositoryImpl.getMeterDataFromDateToDate(meterData1.getFrom(), meterData1.getTo());
        Assert.assertNotNull(meterDataList);
        Assert.assertEquals(meterDataList.get(0).getCustomer_id(), meterData1.getCustomer_id());
    }

    @Test
    void getMeterDataById() throws Exception {
        MeterData meterData =  meterDataRepositoryImpl.getMeterDataById(meterData1.getId());
        Assert.assertEquals(meterData.getCustomer_id(), meterData1.getCustomer_id());
        Assert.assertEquals(meterData.getId(), meterData1.getId());
        Assert.assertEquals(meterData.getMeter_id(), meterData1.getMeter_id());
        Assert.assertEquals(meterData.getFrom(), meterData1.getFrom());
        Assert.assertEquals(meterData.getTo(), meterData1.getTo());
    }

    @Test
    @Transactional
    @Rollback(true)
    void saveMeterData() throws Exception{
        MeterData savedMeterData = meterDataRepositoryImpl.saveMeterData(meterData3);
        Assert.assertEquals(savedMeterData.getCustomer_id(), meterData3.getCustomer_id());
        Assert.assertEquals(savedMeterData.getId(), meterData3.getId());
    }

    @Test
    void getMeterDataFromDateToDateByMeterId() {
        List<MeterData> meterDataList = meterDataRepositoryImpl.getMeterDataFromDateToDateByMeterId(meterData2.getFrom(),meterData2.getTo(),meterData2.getMeter_id());
        Assert.assertEquals(meterDataList.get(0).getId(), meterData2.getId());
    }

    @Test
    void getMeterDataFromDateToDateByCustomerId() {
        List<MeterData> meterDataList = meterDataRepositoryImpl.getMeterDataFromDateToDateByMeterId(meterData1.getFrom(),meterData1.getTo(),meterData1.getMeter_id());
        Assert.assertEquals(meterDataList.get(0).getId(), meterData1.getId());
    }

    private MeterData meterData1 = new MeterData(
            UUID.fromString("bf016460-eae3-4182-8cb1-579738b95702"),
            "test123",
            "testing123",
            "Hour",
            Instant.parse("2005-08-09T00:00:00Z"),
            Instant.parse("2005-08-09T23:00:00Z")
    );

    private MeterData meterData2 = new MeterData(
            UUID.fromString("61730b10-78da-4458-9611-95c31ecc4546"),
            "test234",
            "testing234",
            "Hour",
            Instant.parse("2009-08-09T00:00:00Z"),
            Instant.parse("2009-08-09T23:00:00Z")
    );
    private MeterData meterData3 = new MeterData(
            UUID.fromString("61730b10-78da-4458-8cb1-579738b95702"),
            "test345",
            "testing345",
            "Hour",
            Instant.parse("2018-08-09T00:00:00Z"),
            Instant.parse("2018-08-09T23:00:00Z")
    );
}