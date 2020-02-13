package com.example.MeterMaid.controller;

import com.example.MeterMaid.Model.MeterData;
import com.example.MeterMaid.Model.MeterDataWithValues;
import com.example.MeterMaid.Model.MeterValue;
import com.example.MeterMaid.dao.MeterDataRepository;
import com.example.MeterMaid.dao.MeterValueRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.util.*;

import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest
class MeterControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private MeterController meterController;

    @MockBean
    private MeterDataRepository meterDataRepository;

    @MockBean
    private MeterValueRepository meterValueRepository;

    @Test
    void contextLoads() {
    }
    @Test
    void getAllMeterData() throws Exception {
        List<MeterData> listOfMeterData = getListOfMeterData();
        when(meterDataRepository.GetAll()).thenReturn(listOfMeterData);
        when(meterValueRepository.getMeterValuesByMeterDataId(listOfMeterData.get(0).getId())).thenReturn(getListOfMeterValuesFromMeterData(0));
        when(meterValueRepository.getMeterValuesByMeterDataId(listOfMeterData.get(1).getId())).thenReturn(getListOfMeterValuesFromMeterData(1));
        when(meterValueRepository.getMeterValuesByMeterDataId(listOfMeterData.get(2).getId())).thenReturn(getListOfMeterValuesFromMeterData(2));

        mockMvc.perform(get("/metermaid/api/meterdata/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].meter_id").value(getListOfMeterData().get(0).getMeter_id()))
                .andExpect(jsonPath("$[1].meter_id").value(getListOfMeterData().get(1).getMeter_id()))
                .andExpect(jsonPath("$[2].meter_id").value(getListOfMeterData().get(2).getMeter_id()))
        ;

    }
    @Test
    void testGetMeterDataWithValuesByDate() throws Exception{
        List<MeterData> listOfMeterData = getListOfMeterData();
        listOfMeterData.remove(0);
        listOfMeterData.remove(1);

        when(meterDataRepository.GetMeterDataFromDateToDate(Instant.parse("2018-08-10T00:00:00Z"), Instant.parse("2018-08-10T23:00:00Z"))).thenReturn(listOfMeterData);
        when(meterValueRepository.getMeterValuesByMeterDataId(listOfMeterData.get(0).getId())).thenReturn(getListOfMeterValuesFromMeterData(0));

        mockMvc.perform(get("/metermaid/api/meterdata/2018-08-10T00:00:00Z/2018-08-10T23:00:00Z/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].meter_id").value(listOfMeterData.get(0).getMeter_id()))
                .andExpect(jsonPath("$[0].from").value(listOfMeterData.get(0).getFrom().toString()))
                .andExpect(jsonPath("$[0].to").value(listOfMeterData.get(0).getTo().toString()))
        ;
    }

    @Test
    void testGetMeterDataSumFromDateToDateByMeterId() throws Exception{
        List<MeterData> listOfMeterData = getListOfMeterData();
        listOfMeterData.remove(2);
        listOfMeterData.remove(1);
        List<MeterValue> listOfMeterValues = getListOfMeterValuesFromMeterData(0);
        double sumOfValues = getSumOfValues(listOfMeterValues);
        System.out.println(sumOfValues);
        when(meterDataRepository.getMeterDataFromDateToDateByMeterId(Instant.parse("2018-08-09T00:00:00Z"), Instant.parse("2018-08-09T23:00:00Z"),"test123")).thenReturn(listOfMeterData);
        when(meterValueRepository.getMeterValueFromDateToDateByMeterId(Instant.parse("2018-08-09T00:00:00Z"), Instant.parse("2018-08-09T23:00:00Z"),"test123")).thenReturn(listOfMeterValues);


        mockMvc.perform(get("/metermaid/api/meterdata/2018-08-09T00:00:00Z/2018-08-09T23:00:00Z/meterid:test123/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum").value(sumOfValues))
                .andExpect(jsonPath("$.from").value(listOfMeterData.get(0).getFrom().toString()))
                .andExpect(jsonPath("$.to").value(listOfMeterData.get(0).getTo().toString()))
                .andExpect(jsonPath("$.id").value(listOfMeterData.get(0).getMeter_id()))


        ;
    }

    @Test
    void testGetMeterDataSumFromDateToDateByCustomerId() throws Exception{
        List<MeterData> listOfMeterData = getListOfMeterData();
        listOfMeterData.remove(0);
        List<MeterValue> listOfMeterValues = getListOfMeterValuesFromMeterData(1);
        listOfMeterValues.addAll(getListOfMeterValuesFromMeterData(2));

        double sumOfValues = getSumOfValues(listOfMeterValues);


        when(meterValueRepository.getMeterValueFromDateToDateByCustomerId(Instant.parse("2018-08-09T00:00:00Z"), Instant.parse("2018-08-09T23:00:00Z"),"testing123")).thenReturn(listOfMeterValues);

        mockMvc.perform(get("/metermaid/api/meterdata/2018-08-09T00:00:00Z/2018-08-09T23:00:00Z/customerid:testing123/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum").value(sumOfValues))
                .andExpect(jsonPath("$.from").value(listOfMeterData.get(0).getFrom().toString()))
                .andExpect(jsonPath("$.to").value(listOfMeterData.get(0).getTo().toString()))
                .andExpect(jsonPath("$.id").value(listOfMeterData.get(0).getCustomer_id()))

        ;


    }

    @Test
    void testSaveMeterDataAndValues() throws Exception{
        MeterData expectedReturnedMeterData = new MeterData(returnExpectedJsonMeterDataWithValues());
        List<MeterValue> expectedReturnedMeterValues = returnExpectedMeterValuesFromJson();

        when(meterDataRepository.SaveMeterData((MeterData) notNull())).thenReturn(expectedReturnedMeterData);


        for (int i = 0; i <returnExpectedMeterValuesFromJson().size() ; i++) {
            when(meterValueRepository.saveMeterValue((MeterValue)notNull())).thenReturn(expectedReturnedMeterValues.get(1));
        }





        mockMvc.perform( MockMvcRequestBuilders
                .post("/metermaid/api/meterdata/")
                .content(returnJsonStringForCreation())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.from").value(expectedReturnedMeterData.getFrom().toString()))
                .andExpect(jsonPath("$.to").value(expectedReturnedMeterData.getTo().toString()))
                .andExpect(jsonPath("$.customer_id").value(expectedReturnedMeterData.getCustomer_id()))
                .andExpect(jsonPath("$.values").isNotEmpty())
                ;
    }





    private List<MeterData> getListOfMeterData(){
        MeterData meterdata1 = new MeterData(
                UUID.randomUUID(),
                "test123",
                "testing123",
                "Hour",
                Instant.parse("2018-08-09T00:00:00Z"),
                Instant.parse("2018-08-09T23:00:00Z")
        );
        MeterData meterdata2 = new MeterData(
                UUID.randomUUID(),
                "test234",
                "testing123",
                "Hour",
                Instant.parse("2018-08-09T00:00:00Z"),
                Instant.parse("2018-08-09T23:00:00Z")
        );
        MeterData meterdata3 = new MeterData(
                UUID.randomUUID(),
                "test345",
                "testing234",
                "Hour",
                Instant.parse("2018-08-10T00:00:00Z"),
                Instant.parse("2018-08-10T23:00:00Z")
        );
        List<MeterData> listOfMeterData = new ArrayList<MeterData>();
        listOfMeterData.add(meterdata1);
        listOfMeterData.add(meterdata2);
        listOfMeterData.add(meterdata3);
        return listOfMeterData;
    }

    private List<MeterValue> getListOfMeterValuesFromMeterData(int meterDataTestValue){
        List<MeterValue> meterValues = new ArrayList<>();
        for (int i = 0; i < 23; i++) {
            meterValues.add(new MeterValue(
                    getListOfMeterData().get(meterDataTestValue).getId(),
                    getListOfMeterData().get(meterDataTestValue).getMeter_id(),
                    getListOfMeterData().get(meterDataTestValue).getCustomer_id(),
                    getListOfMeterData().get(meterDataTestValue).getFrom().plusSeconds(3600*i),
                    getRandomDouble().doubleValue()
            ));
        }
        return meterValues;

    }
    private Double getRandomDouble(){
        Random r = new Random();
        return (5 + (70 - 5) * r.nextDouble());
    }

    private double getSumOfValues(List<MeterValue> values){
        double sum = 0;
        for (int i = 0; i <values.size() ; i++) {
            sum += values.get(i).getValue();
        }
        return sum;
    }


    private String returnJsonStringForCreation() throws Exception{
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


    private List<MeterValue> returnExpectedMeterValuesFromJson(){
        double[] values = {85.4, 45.4, 50.4, 22.5, 12.4, 12.4, 16.4, 70.4, 45.4, 35.4, 20.4, 23.4, 12.4, 12.4,35.4, 55.4, 7.4, 45.4,32.4, 55.4,7.4,6.5,22.4,12.4};
        List<MeterValue> meterValues = new ArrayList<>();

        for (int i = 0; i <= 23; i++) {
            meterValues.add(new MeterValue(
                    returnExpectedJsonMeterDataWithValues().getId(),
                    returnExpectedJsonMeterDataWithValues().getMeter_id(),
                    returnExpectedJsonMeterDataWithValues().getCustomer_id(),
                    returnExpectedJsonMeterDataWithValues().getFrom().plusSeconds(3600*i),
                    values[i]
            ));
        }
        return meterValues;
    }

    private MeterDataWithValues returnExpectedJsonMeterDataWithValues() {
        double[] values = {85.4, 45.4, 50.4, 22.5, 12.4, 12.4, 16.4, 70.4, 45.4, 35.4, 20.4, 23.4, 12.4, 12.4,35.4, 55.4, 7.4, 45.4,32.4, 55.4,7.4,6.5,22.4,12.4};
        Map<Instant, Double> returnedHashMapWithValues = new HashMap<>();
        for (int i = 0; i < values.length; i++) {
            returnedHashMapWithValues.put(
                    Instant.parse("2019-08-09T00:00:00Z").plusSeconds(i*3600),
                    values[i]
            );
        }
        return new MeterDataWithValues(
                "123hhh",
                "123fff",
                "Hour",
                Instant.parse("2019-08-09T00:00:00Z"),
                Instant.parse("2019-08-09T23:00:00Z"),
                returnedHashMapWithValues);
    }


}
