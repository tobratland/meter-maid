package com.example.meterMaid;

import com.example.meterMaid.Model.MeterData;
import com.example.meterMaid.Model.MeterValue;
import com.example.meterMaid.dao.MeterDataRepositoryImpl;
import com.example.meterMaid.dao.MeterValueRepositoryImpl;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback(true)
public class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MeterDataRepositoryImpl meterDataRepositoryImpl;

    @Autowired
    private MeterValueRepositoryImpl meterValueRepositoryImpl;


    @BeforeEach
    void setUp(){
        MeterData meterData0 = getListOfMeterDataForTesting().get(0);
        MeterData meterData1 = getListOfMeterDataForTesting().get(1);

        meterDataRepositoryImpl.saveMeterData(meterData0);
        meterDataRepositoryImpl.saveMeterData(meterData1);
        meterValueRepositoryImpl.saveMeterValues(getListOfMeterValuesFromMeterDataForTesting(0));
        meterValueRepositoryImpl.saveMeterValues(getListOfMeterValuesFromMeterDataForTesting(1));
        meterValueRepositoryImpl.saveMeterValues(getListOfMeterValuesFromMeterDataForTesting(2));




    }
    @Test
    void testGetAll() throws Exception {
        this.mockMvc.perform(get("/metermaid/api/meterdata/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("test234")))
                .andExpect(content().string(containsString("testing123")))
                .andExpect(content().string(containsString("values")))
                .andExpect(content().string(containsString(getListOfMeterDataForTesting().get(0).getFrom().plusSeconds(3600).toString())))
        ;
    }

    @Test
    void testGetByDate() throws Exception {
        this.mockMvc.perform(get("/metermaid/api/meterdata/2018-08-09T00:00:00Z/2018-08-09T23:00:00Z/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("test123")))
                .andExpect(content().string(containsString("testing123")))
                .andExpect(content().string(containsString("values")))
                .andExpect(content().string(not(containsString("test234"))))
                .andExpect(content().string(containsString(getListOfMeterDataForTesting().get(0).getFrom().plusSeconds(3600).toString())))

        ;
    }

    @Test
    void testGetByDateAndMeterID() throws Exception{
        this.mockMvc.perform(get("/metermaid/api/meterdata/2018-08-10T00:00:00Z/2018-08-10T23:00:00Z/meterid:test234/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Sum for meter: test234, in period: 2018-08-10T00:00:00Z to: 2018-08-10T23:00:00Z")))
                .andExpect(jsonPath("$.sum").value(greaterThan(0.1)))
        ;
    }

    @Test
    void testGetByDateAndCustomerId() throws Exception{
        this.mockMvc.perform(get("/metermaid/api/meterdata/2018-08-09T00:00:00Z/2018-08-10T23:00:00Z/customerid:testing123/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Sum for customer: testing123, in period: 2018-08-09T00:00:00Z to: 2018-08-10T23:00:00Z")))
                .andExpect(jsonPath("$.sum").value(greaterThan(0.1)))
        ;
    }

    @Test
    void testSaveMeterDataWithValues() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                .post("/metermaid/api/meterdata/")
                .content(returnJsonStringForCreationTesting())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.from").value("2019-08-09T00:00:00Z"))
                .andExpect(jsonPath("$.to").value("2019-08-09T23:00:00Z"))
                .andExpect(jsonPath("$.customer_id").value("123fff"))
                .andExpect(jsonPath("$.values").isNotEmpty())
        ;
    }

    private List<MeterData> getListOfMeterDataForTesting(){
        MeterData meterdata1 = new MeterData(
                UUID.fromString("90314011-6a7a-42cc-93ef-e22627db5043"),
                "test123",
                "testing123",
                "Hour",
                Instant.parse("2018-08-09T00:00:00Z"),
                Instant.parse("2018-08-09T23:00:00Z")
        );
        MeterData meterdata2 = new MeterData(
                UUID.fromString("0d848322-6d7d-42de-b914-595ed2c9d7ff"),
                "test234",
                "testing123",
                "Hour",
                Instant.parse("2018-08-10T00:00:00Z"),
                Instant.parse("2018-08-10T23:00:00Z")
        );
        MeterData meterdata3 = new MeterData(
                UUID.fromString("95814421-257e-4657-9316-60054e55d246"),
                "test345",
                "testing234",
                "Hour",
                Instant.parse("2018-08-11T00:00:00Z"),
                Instant.parse("2018-08-11T23:00:00Z")
        );
        List<MeterData> listOfMeterData = new ArrayList<MeterData>();
        listOfMeterData.add(meterdata1);
        listOfMeterData.add(meterdata2);
        listOfMeterData.add(meterdata3);
        return listOfMeterData;
    }

    private List<MeterValue> getListOfMeterValuesFromMeterDataForTesting(int meterDataTestValue){
        List<MeterValue> meterValues = new ArrayList<>();
        for (int i = 0; i < 23; i++) {
            meterValues.add(new MeterValue(
                    getListOfMeterDataForTesting().get(meterDataTestValue).getId(),
                    getListOfMeterDataForTesting().get(meterDataTestValue).getMeter_id(),
                    getListOfMeterDataForTesting().get(meterDataTestValue).getCustomer_id(),
                    getListOfMeterDataForTesting().get(meterDataTestValue).getFrom().plusSeconds(3600*i),
                    getRandomDouble().doubleValue()
            ));
        }
        return meterValues;

    }
    private Double getRandomDouble(){
        Random r = new Random();
        return (5 + (70 - 5) * r.nextDouble());
    }
    private String returnJsonStringForCreationTesting() throws Exception{
        return new JSONObject()
                .put("id", "97109855-b6e3-46b0-a6a1-dcc44aeb4ebf")
                .put("meter_id", "123hhh")
                .put("customer_id", "123fff")
                .put("resolution", "Hour")
                .put("from", "2019-08-09T00:00:00Z")
                .put("to", "2019-08-09T23:00:00Z")
                .put("values", new JSONObject() //, , 16.4, 70.4, 45.4, 35.4, 20.4, 23.4, 12.4, 12.4,35.4, 55.4, 7.4, 45.4,32.4, 55.4,7.4,6.5,22.4,12.4
                        .put("2019-08-09T00:00:00Z", 85.4)
                        .put("2019-08-09T01:00:00Z", 45.4)
                        .put("2019-08-09T02:00:00Z", 50.4)
                        .put("2019-08-09T03:00:00Z", 22.5)
                        .put("2019-08-09T04:00:00Z", 12.4)
                        .put("2019-08-09T05:00:00Z", 12.4)
                        .put("2019-08-09T06:00:00Z", 16.4)
                        .put("2019-08-09T07:00:00Z", 70.4)
                        .put("2019-08-09T08:00:00Z", 45.4)
                        .put("2019-08-09T09:00:00Z", 35.4)
                        .put("2019-08-09T10:00:00Z", 20.4)
                        .put("2019-08-09T11:00:00Z", 23.4)
                        .put("2019-08-09T12:00:00Z", 12.4)
                        .put("2019-08-09T13:00:00Z", 12.4)
                        .put("2019-08-09T14:00:00Z", 35.4)
                        .put("2019-08-09T15:00:00Z", 55.4)
                        .put("2019-08-09T16:00:00Z", 7.4)
                        .put("2019-08-09T17:00:00Z", 45.4)
                        .put("2019-08-09T18:00:00Z", 32.4)
                        .put("2019-08-09T19:00:00Z", 55.4)
                        .put("2019-08-09T20:00:00Z", 7.4)
                        .put("2019-08-09T21:00:00Z", 6.5)
                        .put("2019-08-09T22:00:00Z", 22.4)
                        .put("2019-08-09T23:00:00Z", 12.4)).toString();
    }

}
